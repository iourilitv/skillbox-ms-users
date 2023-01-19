package com.example.microservices.users.repository;

import com.example.microservices.users.entity.Follow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends CrudRepository<Follow, Long> {

    Optional<Follow> findByFollowingIdAndFollowerId(Long followingId, Long followerId);

    List<Follow> findAllByFollowingId(Long followingId);

    List<Follow> findAllByFollowerId(Long followerId);

    @Modifying
    @Query("UPDATE Follow f SET f.refersDeletedUser = :flag WHERE f.followingId = :userId OR f.followerId = :userId")
    void setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(@Param("userId") Long userId, @Param("flag") boolean refersDeletedUser);
}
