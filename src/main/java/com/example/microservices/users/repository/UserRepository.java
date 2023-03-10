package com.example.microservices.users.repository;

import com.example.microservices.users.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.nickname = :nickname", nativeQuery = true)
    Optional<User> findByNicknameIncludingDeleted(@Param("nickname") String nickname);
}
