package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.CityDTO;
import com.example.microservices.users.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityDTO toDTO(City entity);

    City toEntity(CityDTO dto);
}
