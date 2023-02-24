package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.entity.Follow;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    FollowDTO toDTO(Follow entity);

    default Follow toEntity(@NonNull FollowDTO dto) {
        Follow follow = new Follow(dto.getFollowingId(), dto.getFollowerId());
        follow.setFollowedAt(dto.getFollowedAt());
        return follow;
    }

    List<FollowDTO> toDTOList(List<Follow> entityList);

    List<Follow> toEntityList(List<FollowDTO> dtoList);
}
