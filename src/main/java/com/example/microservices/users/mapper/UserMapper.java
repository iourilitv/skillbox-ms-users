package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CityMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {

    public abstract UserDTO toDTO(User entity);

    public abstract User toEntity(UserDTO userDTO, @MappingTarget User user);

    //This is to use not default constructor instead of User() for field without setter, nickName.
    //It only works together the same named abstract method
    //Source: https://github.com/mapstruct/mapstruct/issues/73#issuecomment-548438210
    public final User toEntity(final UserDTO userDTO) {
        return toEntity(userDTO, new User(userDTO.getId(), userDTO.getNickname()));
    }

    public abstract List<UserDTO> toDTOList(List<User> entityList);

    public abstract List<User> toEntityList(List<UserDTO> dtoList);

    @BeforeMapping
    void getNumbers(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setFollowingsNumber(user.getFollowings().size());
        userDTO.setFollowersNumber(user.getFollowers().size());
    }
}
