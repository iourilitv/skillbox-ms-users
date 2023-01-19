package com.example.microservices.users.dto;

import com.example.microservices.users.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Schema(description = "Public data of User", example = "{\n" +
        "  \"id\": 1,\n" +
        "  \"firstName\": \"Yury\",\n" +
        "  \"lastName\": \"Petrov\",\n" +
        "  \"secondName\": \"UpdatedStakanych\",\n" +
        "  \"gender\": \"MALE\",\n" +
        "  \"birthday\": \"2000-11-22T00:00:00.000+00:00\",\n" +
        "  \"currentCity\": {\n" +
        "    \"id\": 1,\n" +
        "    \"name\": \"Moscow\"\n" +
        "  },\n" +
        "  \"nickname\": \"iuric\",\n" +
        "  \"email\": \"y.petrov@mail.com\",\n" +
        "  \"phone\": \"+7(999)123-4567\",\n" +
        "  \"followingsNumber\": 2,\n" +
        "  \"followersNumber\": 1\n" +
        "}")
@ToString
@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String lastName;

    private String secondName;

    @JsonProperty(required = true)
    private Gender gender;

    @JsonProperty(required = true)
    private Date birthday;

    @JsonProperty(required = true)
    private CityDTO currentCity;

    @JsonProperty(required = true)
    private String nickname;

    private String email;

    private String phone;

    private String about;

    private String hardSkills;

    private Integer followingsNumber;

    private Integer followersNumber;

    public UserDTO(String firstName, String lastName, Gender gender, Date birthday, CityDTO currentCity, String nickname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.currentCity = currentCity;
        this.nickname = nickname;
    }
}
