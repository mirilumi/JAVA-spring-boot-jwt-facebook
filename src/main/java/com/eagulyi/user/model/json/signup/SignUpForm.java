
package com.eagulyi.user.model.json.signup;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "email",
        "country",
        "city",
        "educationList",
        "workList"
})
public class SignUpForm extends JsonUserObject {

    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;
    private List<EducationList> educationList = new ArrayList<>();
    @JsonProperty("workList")
    private List<WorkList> workList = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("educationList")
    public List<EducationList> getEducationList() {
        return educationList;
    }

    @JsonProperty("educationList")
    public void setEducationList(List<EducationList> educationList) {
        this.educationList = educationList;
    }

    @JsonProperty("workList")
    public List<WorkList> getWorkList() {
        return workList;
    }

    @JsonProperty("workList")
    public void setWorkList(List<WorkList> workList) {
        this.workList = workList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(firstName).append(lastName).append(email).append(country).append(city).append(educationList).append(workList).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SignUpForm)) {
            return false;
        }
        SignUpForm rhs = ((SignUpForm) other);
        return new EqualsBuilder().append(firstName, rhs.firstName).append(lastName, rhs.lastName).append(email, rhs.email).append(country, rhs.country).append(city, rhs.city).append(educationList, rhs.educationList).append(workList, rhs.workList).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
