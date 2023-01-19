package com.example.microservices.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "Public data of City", example = "{\n" +
        "    \"id\": 1,\n" +
        "    \"name\": \"Moscow\"\n" +
        "  }")
@ToString
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CityDTO {

    @JsonProperty(required = true)
    private Integer id;

    private String name;

    public CityDTO(Integer id) {
        this.id = id;
    }
}
