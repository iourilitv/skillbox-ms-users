package com.example.microservices.users.regress;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

import static com.example.microservices.users.util.UserTestUtils.createUser;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegressionTest {

    private static final String HOST_NAME = "localhost";
    private static final String USERS_RESOURCE_URL = "/users";
    private static final String FOLLOWS_RESOURCE_URL = "/follows";

    @Container private static final PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private CityRepository cityRepository;
    @LocalServerPort private int port;

    private String baseUrl;
    private List<City> cities;

    @BeforeEach
    void setUp() {
        baseUrl = "http://" + HOST_NAME + ":" + port;
        cities = (List<City>) cityRepository.findAll();
    }

    @Test
    void test_regression() throws URISyntaxException {
        var userDto1 = createUser_thenOK(1);
        var userDto2 = createUser_thenOK(2);
        getUser_thenOK(userDto1);
        getUser_thenOK(userDto2);
        getAll_thenOk(userDto1, userDto2);

//        test03_setFollow1For2_thenOK();
//        test04_removeFollow_thenOk();
//        test05_updateUser_thenOK();
//        test06_deleteUser_thenOK();
//        test07_getDeletedUser_thenError404();
//        test08_getNotExistUser_thenError404();
    }

    private UserDTO createUser_thenOK(int userIndex) {
//Создать двух пользователей.
        int cityIndex = new Random().nextInt(cities.size() - 1);
        User userToCreate = createUser(userIndex, cities.get(cityIndex));
        UserDTO userDTOToCreate = toUserDTO(userToCreate);
        var url = baseUrl + USERS_RESOURCE_URL;
        var response = restTemplate.postForEntity(url, userDTOToCreate, UserDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO actual = response.getBody();
        assertNotNull(actual);
        assertNotNull(actual.getId());
        return actual;
    }

    private void getUser_thenOK(UserDTO origin) throws URISyntaxException {
//Успешно получить их по ID.
        var url = new URI(baseUrl + USERS_RESOURCE_URL + "/" + origin.getId());
        var response = restTemplate.getForEntity(url, UserDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(origin, response.getBody());
    }

    private void getAll_thenOk(UserDTO... expectedArr) throws URISyntaxException {
//Провести поиск пользователей.
        var url = new URI(baseUrl + USERS_RESOURCE_URL);
        var response = restTemplate.getForEntity(url, UserDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO[] actualArr = response.getBody();
        Assertions.assertThat(actualArr).containsSequence(expectedArr);
    }

    private void test03_setFollow1For2_thenOK() {
//Подписать одного пользователя на другого.
//Проверить изменившиеся данные.

    }

    private void test04_removeFollow_thenOk() {
//Удалить подписку.
//Проверить изменившиеся данные.
    }

    private void test05_updateUser_thenOK() {
//Проверить частичное изменение данных.
//Проверить изменившиеся данные.
    }

    private void test06_deleteUser_thenOK() {
//Удалить пользователей.
    }

    private void test07_getDeletedUser_thenError404() {
//Получить ошибку 404 по ID пользователей.
    }

    private void test08_getNotExistUser_thenError404() {
//Получить ошибку 404 по ID пользователей.
    }
}