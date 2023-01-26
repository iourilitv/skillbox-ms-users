package com.example.microservices.users.mapper;

import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.util.UserTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.UserTestUtils.fillUpTestUsers;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class UserMapperTest {
    private static final int TEST_LIST_SIZE = 10;

    CityMapper cityMapper = Mappers.getMapper(CityMapper.class);
    UserMapper userMapper = new UserMapperImpl(cityMapper);

    private final List<User> testUsers = new ArrayList<>(TEST_LIST_SIZE);

    @BeforeEach
    void setUp() {
        fillUpTestUsers(TEST_LIST_SIZE, testUsers);
    }

    @AfterEach
    void tearDown() {
        testUsers.clear();
    }

    @Test
    void test1_givenEntity_thenCorrect_toDTO() {
        User user = testUsers.get(0);
        UserDTO expectedDTO = toUserDTO(user);
        UserDTO actualDTO = userMapper.toDTO(user);
        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    void test2_givenDTO_thenCorrect_toEntity() {
        User expected = testUsers.get(0);
        UserDTO userDTO = toUserDTO(expected);
        User actual = userMapper.toEntity(userDTO);
        assertEquals(expected, actual);
    }

    @Test
    void test3_givenEntityList_thenCorrect_toDTOList() {
        List<UserDTO> expectedList = testUsers.stream().map(UserTestUtils::toUserDTO).collect(Collectors.toList());
        List<UserDTO> actualList = userMapper.toDTOList(testUsers);
        assertIterableEquals(expectedList, actualList);
    }

    @Test
    void test4_givenDTOList_thenCorrect_toEntityList() {
        List<UserDTO> dtoList = testUsers.stream().map(UserTestUtils::toUserDTO).collect(Collectors.toList());
        List<User> actualList = userMapper.toEntityList(dtoList);
        assertIterableEquals(testUsers, actualList);
    }
}