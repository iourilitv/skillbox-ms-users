package com.example.microservices.users.smoke;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.UserDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.example.microservices.users.util.UserTestUtils.createUser;
import static com.example.microservices.users.util.UserTestUtils.toUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase
@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmokeTest {

    @Autowired private CityRepository cityRepository;
    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort private int port;

    private String url;
    private List<City> cities;

    @BeforeEach
    public void setUp() {
        url = "http://localhost:" + port + "/users/";
        cities = (List<City>) cityRepository.findAll();
    }

    @Test
    void createUser_then_getAll_then_OK() {
        User userToCreate = createUser(99, cities.get(1));
        UserDTO userDTOToCreate = toUserDTO(userToCreate);
        var responseCreateUser = restTemplate.postForEntity(url, userDTOToCreate, String.class);
        assertEquals(HttpStatus.OK, responseCreateUser.getStatusCode());

        var responseGetAll = restTemplate.getForEntity(url, UserDTO[].class);
        assertEquals(HttpStatus.OK, responseGetAll.getStatusCode());
        assertNotNull(responseGetAll.getBody());
        var body = responseGetAll.getBody();
        assertEquals(1, body.length);
        assertEquals(userToCreate.getNickname(), body[0].getNickname());
    }
}
