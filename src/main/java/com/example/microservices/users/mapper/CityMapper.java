package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.CityDTO;
import com.example.microservices.users.entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDTO toDTO(City entity);

    City toEntity(CityDTO dto);
}
