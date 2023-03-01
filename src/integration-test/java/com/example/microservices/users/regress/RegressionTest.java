package com.example.microservices.users.regress;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestPropertySource(locations = "classpath:application-integration-test.properties")
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration(initializers = {RegressionTest.Initializer.class})
@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegressionTest {

    private static final String HOST_NAME = "localhost";
    private static final String USERS_RESOURCE_URL = "/users";
    private static final String FOLLOWS_RESOURCE_URL = "/follows";

    @Container public static PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
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
        getAllUsers_thenOk(userDto1, userDto2);
        var followDto1For2 = followUp1For2_thenOK(userDto1, userDto2);
        getAllFollows_thenOK(followDto1For2);
        getAllFollowings_thenOK(followDto1For2.getFollowerId(), followDto1For2);
        getAllFollowers_thenOK(followDto1For2.getFollowingId(), followDto1For2);

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

    private void getAllUsers_thenOk(UserDTO... expectedArr) throws URISyntaxException {
//Провести поиск пользователей.
        var url = new URI(baseUrl + USERS_RESOURCE_URL);
        var response = restTemplate.getForEntity(url, UserDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private FollowDTO followUp1For2_thenOK(UserDTO userDto1, UserDTO userDto2) {
//Подписать одного пользователя на другого.
    FollowDTO followDtoToCreate = new FollowDTO(userDto2.getId(), userDto1.getId());
    var url = baseUrl + FOLLOWS_RESOURCE_URL;
    var response = restTemplate.postForEntity(url, followDtoToCreate, FollowDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    FollowDTO actual = response.getBody();
    assertNotNull(actual);
    assertNotNull(actual.getId());
    return actual;
    }

    private void getAllFollows_thenOK(FollowDTO... expectedArr) {
//Проверить изменившиеся данные.
        var url = baseUrl + FOLLOWS_RESOURCE_URL;
        var response = restTemplate.getForEntity(url, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private void getAllFollowings_thenOK(long followerId, FollowDTO... expectedArr) {
//Проверить изменившиеся данные.
        var url = String.format(baseUrl + FOLLOWS_RESOURCE_URL + "/followings/%d", followerId);
        var response = restTemplate.getForEntity(url, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private void getAllFollowers_thenOK(long followingId, FollowDTO... expectedArr) {
//Проверить изменившиеся данные.
        var url = String.format(baseUrl + FOLLOWS_RESOURCE_URL + "/followers/%d", followingId);
        var response = restTemplate.getForEntity(url, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
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