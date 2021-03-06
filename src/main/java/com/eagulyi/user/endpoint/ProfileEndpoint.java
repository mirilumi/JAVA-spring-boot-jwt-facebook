package com.eagulyi.user.endpoint;

import com.eagulyi.common.ServletUtil;
import com.eagulyi.security.auth.jwt.JwtAuthenticationToken;
import com.eagulyi.security.model.UserContext;
import com.eagulyi.user.entity.User;
import com.eagulyi.user.model.json.signup.SignUpForm;
import com.eagulyi.user.service.UserService;
import com.eagulyi.user.service.util.converter.SignUpFormUserConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileEndpoint {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final SignUpFormUserConverter signUpFormUserConverter;
    private final ServletUtil servletUtil;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileEndpoint.class);

    @Autowired
    public ProfileEndpoint(ObjectMapper objectMapper, UserService userService, SignUpFormUserConverter signUpFormUserConverter, ServletUtil servletUtil) {
        this.objectMapper = objectMapper;
        this.signUpFormUserConverter = signUpFormUserConverter;
        this.servletUtil = servletUtil;
        this.objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        this.userService = userService;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public void getUserProfile(JwtAuthenticationToken token, HttpServletResponse response) {
        UserContext userContext = (UserContext) token.getPrincipal();
        LOG.debug("Requesting user %s timestamp %s", userContext.getUsername(), LocalDateTime.now());
        User user = userService.getByUsername(userContext.getUsername());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("username", user.getUsername());
        responseMap.put("first_name", user.getFirstName());
        responseMap.put("last_name", user.getLastName());
        response.setContentType("application/json;charset=UTF-8");
        servletUtil.writeServletResponse(response, responseMap);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userString = request.getReader().lines().collect(Collectors.joining());
            SignUpForm signUpUserData = objectMapper.readValue(userString, SignUpForm.class);
            userService.save(signUpFormUserConverter.convert(signUpUserData));
        } catch (IOException ioe) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain; charset=UTF-8");
        }
    }


}