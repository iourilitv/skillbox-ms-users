package com.example.microservices.users.controller;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import com.example.microservices.users.util.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.MapperTestUtils.initMapper;
import static com.example.microservices.users.util.UserTestUtils.createUser;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Module tests
 */

@Transactional
@ActiveProfiles(profiles = "integration-test")
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@TestMethodOrder(value = MethodOrderer.MethodName.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = UsersApplication.class)
@ContextConfiguration(initializers = {ITestUserController.Initializer.class}) // This is only for example another way of implementing
@Testcontainers(disabledWithoutDocker = true)
class ITestUserController {
    private static final int TEST_LIST_SIZE = 5;

    @Container
    public static PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
    private @Autowired MockMvc mockMvc;
    private @Autowired EntityManager entityManager;
    private @Autowired CityRepository cityRepository;
    private static final ObjectMapper mapper = initMapper();

    private List<City> cities;
    private final List<User> testUsers = new ArrayList<>(TEST_LIST_SIZE);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cities = (List<City>) cityRepository.findAll();
        fillUpTestUsers();
        storeTestData();
    }

    @AfterEach
    void tearDown() {
        testUsers.clear();
    }

    @Test
    void test1_thenCorrect_getAll() throws Exception {
        List<UserDTO> dtoList = testUsers.stream().map(UserTestUtils::toUserDTO).collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(dtoList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
        Assertions.assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void test21_givenExistUserId_thenCorrect_getUser() throws Exception {
        User user = testUsers.get(0);
        UserDTO userDTO = toUserDTO(user);

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
        String expected = String.format("User(id: %s, nickname: %s) has been updated successfully", userDTO.getId(), userToUpdate.getNickname());
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
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test41_givenNotExistUser_thenCorrect_createUser() throws Exception {
        User userToCreate = createUser(99, cities.get(1));
        UserDTO userDTO = toUserDTO(userToCreate);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        User savedUser = findUserByNickname(userToCreate.getNickname());
        String expected = String.format("New user(nickname: %s) has been saved with id: %s", userToCreate.getNickname(), savedUser.getId());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    void test42_givenExistUser_thenError_createUser() throws Exception {
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        User userToCreate = testUsers.get(0);
        UserDTO userDTO = toUserDTO(userToCreate);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDTO))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    @Test
    void test51_givenExistAndUndeletedUser_thenCorrect_deleteUser() throws Exception {
        User userToDelete = testUsers.get(0);
        String expected = String.format("User(id: %s, nickname: %s) has been deleted", userToDelete.getId(), userToDelete.getNickname());
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
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", isDeletedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(expectedHttpStatus.value(), response.getStatus());
    }

    private User findUserByNickname(String nickname) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = cb.equal(root.get("nickname"), nickname);
        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private void fillUpTestUsers() {
        for (int i = 0; i < TEST_LIST_SIZE; i++) {
            testUsers.add(createUser(i, cities.get(i % cities.size())));
        }
    }

    private void storeTestData() {
        testUsers.forEach(this::storeUser);
    }

    private void storeUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    // This is only for example another way of implementing. The best practice way is in ITestFollowController
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}