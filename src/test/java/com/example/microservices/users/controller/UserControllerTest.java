package com.example.microservices.users.controller;

import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.mapper.UserMapper;
import com.example.microservices.users.service.UserService;
import com.example.microservices.users.util.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.UserTestUtils.fillUpTestUsers;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests.
 * This version is instead of not working example with
 * resultActions.andExpect(jsonPath("$", Matchers.equalToObject(json)))
 * because when put the json to matcher method it transforms to String like this:
 * Expected: "[{\"id\":11,\"firstName\":\"test_firstName1\",\"lastName\":\"test_lastName1\",\"secondName\":\"test_secondName1\",\"gender\":\"FEMALE\",...
 * But actual: [{"id":11,"firstName":"test_firstName1","lastName":"test_lastName1","secondName":"test_secondName1","gender":"FEMALE",...
 */

@WebMvcTest(UserController.class)
@TestMethodOrder(value = MethodOrderer.MethodName.class)
class UserControllerTest {
    private static final int TEST_LIST_SIZE = 5;

    private @MockBean UserMapper userMapper;
    private @MockBean UserService userService;

    private @Autowired MockMvc mockMvc;
    private @Autowired ObjectMapper mapper;

    private final List<User> testUsers = new ArrayList<>(TEST_LIST_SIZE);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new JsonMapper();
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));    //"birthday":"2022-12-02T06:55:59.842+00:00"
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));                        // timestamp in db without time zone
        fillUpTestUsers(TEST_LIST_SIZE, testUsers);
    }

    @AfterEach
    void tearDown() {
        testUsers.clear();
    }

    @Test
    void test1_thenCorrect_getAll() throws Exception {
        when(userService.getAll()).thenReturn(testUsers);
        List<UserDTO> dtoList = testUsers.stream().map(UserTestUtils::toUserDTO).collect(Collectors.toList());
        when(userMapper.toDTOList(Mockito.anyList())).thenReturn(dtoList);

        String expectedJson = mapper.writeValueAsString(dtoList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test21_givenExistUserId_thenCorrect_getUser() throws Exception {
        User user = testUsers.get(0);
        when(userService.getUser(user.getId())).thenReturn(user);
        UserDTO userDTO = toUserDTO(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        String expectedJson = mapper.writeValueAsString(userDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test22_givenNotExistUserId_thenError_getUser() throws Exception {
        long notExistId = 9999L;
        HttpStatus expectedHttpStatus = HttpStatus.NOT_FOUND;
        when(userService.getUser(notExistId)).thenThrow(new ResponseStatusException(expectedHttpStatus));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test31_givenUpdatedUser_thenCorrect_updateUser() throws Exception {
        User userToUpdate = testUsers.get(0);
        userToUpdate.setSecondName("updated " + userToUpdate.getSecondName());
        UserDTO userDTO = toUserDTO(userToUpdate);
        when(userMapper.toEntity(userDTO)).thenReturn(userToUpdate);
        String expected = String.format("User(id: %s, nickname: %s) has been updated successfully", userDTO.getId(), userToUpdate.getNickname());
        when(userService.updateUser(userToUpdate)).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userDTO.getId())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    void test32_givenNotExistUpdatedUser_thenError_updateUser() throws Exception {
        long notExistId = 9999L;
        HttpStatus expectedHttpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        User userToUpdate = testUsers.get(0);
        userToUpdate.setSecondName("updated " + userToUpdate.getSecondName());
        UserDTO userDTO = toUserDTO(userToUpdate);
        when(userMapper.toEntity(userDTO)).thenReturn(userToUpdate);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test41_givenNotExistUser_thenCorrect_createUser() throws Exception {
        User userToCreate = testUsers.get(0);
        UserDTO userDTO = toUserDTO(userToCreate);
        userDTO.setId(null);
        when(userMapper.toEntity(userDTO)).thenReturn(userToCreate);
        String expected = String.format("New user(nickname: %s) has been saved with id: %s", userToCreate.getNickname(), userDTO.getId());
        when(userService.createUser(userToCreate)).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    void test42_givenExistUser_thenError_createUser() throws Exception {
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        User userToCreate = testUsers.get(0);
        UserDTO userDTO = toUserDTO(userToCreate);
        userDTO.setId(null);
        when(userMapper.toEntity(userDTO)).thenReturn(userToCreate);
        when(userService.createUser(userToCreate)).thenThrow(new ResponseStatusException(expectedHttpStatus));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test51_givenExistAndUndeletedUser_thenCorrect_deleteUser() throws Exception {
        User userToDelete = testUsers.get(0);
        String expected = String.format("User(id: %s, nickname: %s) has been deleted", userToDelete.getId(), userToDelete.getNickname());
        when(userService.deleteUser(userToDelete.getId())).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    void test52_givenNotExistUser_thenError_deleteUser() throws Exception {
        Long notExistId = 999L;
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        when(userService.deleteUser(notExistId)).thenThrow(new ResponseStatusException(expectedHttpStatus));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test53_givenExistAndIsDeletedUser_thenError_deleteUser() throws Exception {
        User isDeletedUser = testUsers.get(0);
        isDeletedUser.setDeleted(true);
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        when(userService.deleteUser(isDeletedUser.getId())).thenThrow(new ResponseStatusException(expectedHttpStatus));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", isDeletedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }
}