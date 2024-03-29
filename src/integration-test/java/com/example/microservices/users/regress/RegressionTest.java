package com.example.microservices.users.regress;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import com.example.microservices.users.util.SimplePostgreSQLTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.example.microservices.users.util.UserTestUtils.createUser;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegressionTest {

    private static final String HOST_NAME = "localhost";
    private static final String USERS_RESOURCE_URL = "/users";
    private static final String FOLLOWS_RESOURCE_URL = "/follows";

    @Container static PostgreSQLContainer<?> sqlContainer = SimplePostgreSQLTestContainer.sqlContainer();
    @DynamicPropertySource static void setProperties(DynamicPropertyRegistry registry) {
        SimplePostgreSQLTestContainer.setProperties(registry);
    }

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private CityRepository cityRepository;
    @LocalServerPort private int port;

    private String baseUserUrl;
    private String baseFollowUrl;
    private List<City> cities;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://" + HOST_NAME + ":" + port;
        baseUserUrl = baseUrl + USERS_RESOURCE_URL;
        baseFollowUrl = baseUrl + FOLLOWS_RESOURCE_URL;
        cities = (List<City>) cityRepository.findAll();
    }

    @Test
    void test_regression() {
        var userDto1 = createUser_thenOK(1);
        var userDto2 = createUser_thenOK(2);
        getUser_thenOK(userDto1);
        getUser_thenOK(userDto2);
        getNotExistOrDeletedUser_then_PreconditionError_500(userDto1.getId() + 999);
        getAllUsers_thenOk(userDto1, userDto2);
        var followDto1For2 = followUp1For2_thenOK(userDto1, userDto2);
        checkFollower_thenOk(userDto1.getId(), 1, 0);
        checkFollowing_thenOk(userDto2.getId(), 0, 1);
        getAllFollows_thenOK(followDto1For2);
        getAllFollowings_thenOK(followDto1For2.getFollowerId(), followDto1For2);
        getAllFollowers_thenOK(followDto1For2.getFollowingId(), followDto1For2);
        removeFollow_thenOk(followDto1For2.getId());
        checkFollower_thenOk(userDto1.getId(), 0, 0);
        checkFollowing_thenOk(userDto2.getId(), 0, 0);
        getAllFollows_thenOK();
        getAllFollowings_thenOK(followDto1For2.getFollowerId());
        getAllFollowers_thenOK(followDto1For2.getFollowingId());
        userDto1.setSecondName("updated_" + userDto1.getSecondName());
        updateUser_thenOK(userDto1);
        getUser_thenOK(userDto1);
        deleteUsers_thenOK(userDto1.getId(), userDto2.getId());
        getAllUsers_thenOk();
        getNotExistOrDeletedUser_then_PreconditionError_500(userDto1.getId());
    }

    private UserDTO createUser_thenOK(int userIndex) {
        int cityIndex = new Random().nextInt(cities.size() - 1);
        User userToCreate = createUser(userIndex, cities.get(cityIndex));
        UserDTO userDTOToCreate = toUserDTO(userToCreate);
        var response = restTemplate.postForEntity(baseUserUrl, userDTOToCreate, UserDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO actual = response.getBody();
        assertNotNull(actual);
        assertNotNull(actual.getId());
        return actual;
    }

    private void getUser_thenOK(UserDTO origin) {
        var response = getUserResponse(origin.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(origin, response.getBody());
    }

    private void getAllUsers_thenOk(UserDTO... expectedArr) {
        var response = restTemplate.getForEntity(baseUserUrl, UserDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private FollowDTO followUp1For2_thenOK(UserDTO userDto1, UserDTO userDto2) {
    FollowDTO followDtoToCreate = new FollowDTO(userDto2.getId(), userDto1.getId());
    var response = restTemplate.postForEntity(baseFollowUrl, followDtoToCreate, FollowDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    FollowDTO actual = response.getBody();
    assertNotNull(actual);
    assertNotNull(actual.getId());
    return actual;
    }

    private void checkFollower_thenOk(long followerId, int followingsNumber, int followersNumber) {
        ResponseEntity<UserDTO> response = getUserResponse(followerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var follower = response.getBody();
        assertNotNull(follower);
        assertEquals(followingsNumber, follower.getFollowingsNumber());
        assertEquals(followersNumber, follower.getFollowersNumber());
    }

    private void checkFollowing_thenOk(long followingId, int followingsNumber, int followersNumber) {
        ResponseEntity<UserDTO> response = getUserResponse(followingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var following = response.getBody();
        assertNotNull(following);
        assertEquals(followingsNumber, following.getFollowingsNumber());
        assertEquals(followersNumber, following.getFollowersNumber());
    }

    private void getAllFollows_thenOK(FollowDTO... expectedArr) {
        var response = restTemplate.getForEntity(baseFollowUrl, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private void getAllFollowings_thenOK(long followerId, FollowDTO... expectedArr) {
        var url = String.format(baseFollowUrl + "/followings/%d", followerId);
        var response = restTemplate.getForEntity(url, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private void getAllFollowers_thenOK(long followingId, FollowDTO... expectedArr) {
        var url = String.format(baseFollowUrl + "/followers/%d", followingId);
        var response = restTemplate.getForEntity(url, FollowDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowDTO[] actualArr = response.getBody();
        assertThat(actualArr).containsSequence(expectedArr);
    }

    private void removeFollow_thenOk(long id) {
        var url = String.format(baseFollowUrl + "/%d", id);
        restTemplate.delete(url);
    }

    private void updateUser_thenOK(UserDTO userDTO) {
        var url = String.format(baseUserUrl + "/%d", userDTO.getId());
        restTemplate.put(url, userDTO);
    }

    private void deleteUsers_thenOK(long... ids) {
        Arrays.stream(ids).forEach(id -> {
            var url = String.format(baseUserUrl + "/%d", id);
            restTemplate.delete(url);
        });
    }

    private void getNotExistOrDeletedUser_then_PreconditionError_500(long id) {
        var response = getUserResponse(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private ResponseEntity<UserDTO> getUserResponse(long id) {
        var url = String.format(baseUserUrl + "/%d", id);
        return restTemplate.getForEntity(url, UserDTO.class);
    }
}