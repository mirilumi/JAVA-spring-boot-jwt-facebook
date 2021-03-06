package com.eagulyi.user.service.util.converter;

import com.eagulyi.user.entity.*;
import com.eagulyi.user.model.json.facebook.Concentration;
import com.eagulyi.user.model.json.facebook.FacebookUserData;
import com.eagulyi.user.repository.LocationRepository;
import com.eagulyi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by eugene on 5/26/17.
 */
@Service
public class FacebookUserConverter implements UserConverter<FacebookUserData> {

    private final LocationRepository locationRepository;
    private final UserService userService;

    @Autowired
    public FacebookUserConverter(LocationRepository locationRepository, UserService userService) {
        this.locationRepository = locationRepository;
        this.userService = userService;
    }

    public User convert(FacebookUserData facebookUser) {
        User user = userService.getOrCreateUser(facebookUser.getEmail());
        user.setDataProvider(DataProvider.FACEBOOK);
        user.setUsername(facebookUser.getEmail());
        user.setFirstName(facebookUser.getFirstName());
        user.setLastName(facebookUser.getLastName());
        user.setCreationDate(LocalDateTime.now());
        user.setLocation(getLocationFromMap((Map) facebookUser.getLocation().getAdditionalProperties().get("location")));
        facebookUser.getWork().forEach(e -> {
            Work work = new Work(e.getEmployer().getName(),
                    e.getPosition().getName(),
                    e.getLocation() != null ? getLocationFromString(e.getLocation().getName()) : null,
                    LocalDate.parse(e.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE),
                    e.getEndDate() == null ? null : LocalDate.parse(e.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE));
            user.addWorkItem(work);
        });

        facebookUser.getEducation().forEach(e -> {
            Set<String> specialtiesSet = e.getConcentration().stream().map(Concentration::getName).collect(Collectors.toSet());
            String yearString = e.getYear().getName();

            Education education = new Education(
                    e.getSchool().getName(),
                    e.getType(),
                    yearString.isEmpty() ? null : LocalDate.of(Integer.parseInt(yearString), 1, 1),
                    specialtiesSet);

            user.addEducationItem(education);
        });

        return user;
    }

    private Location getLocationFromMap(Map locationMap) {
        String city = (String) locationMap.get("city");
        String country = (String) locationMap.get("country");

        return locationRepository.findByCity_NameAndCountry_Name(city, country).orElseGet(() -> new Location(city, country));
    }

    private Location getLocationFromString(String locationString) {
        String[] location = locationString.split(", ");
        String city = location[0];
        String country = location[1];

        return location.length == 2 ? locationRepository.findByCity_NameAndCountry_Name(city, country).orElseGet(() -> new Location(city, country)) : null;
    }


}
