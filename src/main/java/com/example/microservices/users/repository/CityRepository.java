package com.example.microservices.users.repository;

import com.example.microservices.users.entity.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Integer> {
}
