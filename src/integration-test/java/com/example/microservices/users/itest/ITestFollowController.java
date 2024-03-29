package com.example.microservices.users.itest;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.repository.CityRepository;
import com.example.microservices.users.repository.FollowRepository;
import com.example.microservices.users.util.FollowTestUtils;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import com.example.microservices.users.util.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.FollowTestUtils.fillUpFollowsForUsers;
import static com.example.microservices.users.util.FollowTestUtils.toFollowDTO;
import static com.example.microservices.users.util.ITestUtils.getJsonStringFile;
import static com.example.microservices.users.util.MapperTestUtils.initMapper;
import static com.example.microservices.users.util.UserTestUtils.fillUpUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Module tests
 */

@Transactional
@ActiveProfiles(profiles = "integration-test")
@TestMethodOrder(value = MethodOrderer.MethodName.class)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(classes = UsersApplication.class)
class ITestFollowController {
    private static final int TEST_USERS_SIZE = 3;
    private static final int TEST_FOLLOWS_SIZE = TEST_USERS_SIZE * TEST_USERS_SIZE;
    private static final String BASE_URL = "/follows";
    private static final String RESOURCE = "Follow";

    @Container private static final PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
    private static final ObjectMapper mapper = initMapper();
    private @Autowired MockMvc mockMvc;
    private @Autowired EntityManager entityManager;
    private @Autowired CityRepository cityRepository;
    private @Autowired FollowRepository followRepository;

    private List<City> cities;
    private final List<User> users = new ArrayList<>(TEST_USERS_SIZE);
    private final List<Follow> follows = new ArrayList<>(TEST_FOLLOWS_SIZE);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cities = (List<City>) cityRepository.findAll();
        fillUpUsers(TEST_USERS_SIZE, users, cities);
        users.forEach(this::storeUser);
        fillUpFollowsForUsers(TEST_FOLLOWS_SIZE, follows, users);
        follows.forEach(this::storeFollow);
    }

    @AfterEach
    void tearDown() {
        users.clear();
        follows.clear();
    }

    @Test
    void test0_givenRequestForHealth_thenCorrect_health() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo("UP")));
    }

    @Test
    void test1_thenCorrect_getAll() throws Exception {
        List<FollowDTO> dtoList = follows.stream().map(FollowTestUtils::toFollowDTO).collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(dtoList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test2_givenFollowerId_thenCorrect_getAllFollowings() throws Exception {
        long followerId = follows.get(0).getFollowerId();
        List<Follow> followings = follows.stream().filter(f -> followerId == f.getFollowerId()).collect(Collectors.toList());
        List<FollowDTO> followingDTOs = followings.stream().map(FollowTestUtils::toFollowDTO).collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(followingDTOs);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/followings/{followerId}", followerId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test3_givenFollowingId_thenCorrect_getAllFollowers() throws Exception {
        long followingId = follows.get(0).getFollowingId();
        List<Follow> followers = follows.stream().filter(f -> followingId == f.getFollowingId()).collect(Collectors.toList());
        List<FollowDTO> followerDTOs = followers.stream().map(FollowTestUtils::toFollowDTO).collect(Collectors.toList());
        String expectedJson = mapper.writeValueAsString(followerDTOs);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/followers/{followingId}", followingId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test41_givenExistFollowId_thenCorrect_getFollow() throws Exception {
        Follow expected = follows.get(0);
        FollowDTO followDTO = toFollowDTO(expected);
        String expectedJson = mapper.writeValueAsString(followDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", expected.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test42_givenNotExistFollowId_thenError_getFollow() throws Exception {
        long notExistId = 9999L;
        String jsonContent = String.format(
                getJsonStringFile("/json/error/business/ResourceNotFound_resp_500.json"),
                RESOURCE,
                RESOURCE,
                notExistId
        );
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isInternalServerError(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test51_givenNew_thenCorrect_createFollow() throws Exception {
        User user1 = UserTestUtils.createUser(TEST_USERS_SIZE * 100 + 1, cities.get(1));
        storeUser(user1);
        User user2 = UserTestUtils.createUser(TEST_USERS_SIZE * 100 + 2, cities.get(2));
        storeUser(user2);
        Follow followToSave = new Follow(user1.getId(), user2.getId());
        FollowDTO followDTO = toFollowDTO(followToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(followDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        Follow savedFollow = followRepository.findByFollowingIdAndFollowerId(followToSave.getFollowingId(),
                followToSave.getFollowerId()).orElse(null);
        assertNotNull(savedFollow);
    }

    @Test
    void test52_givenWithId_thenError_createFollow() throws Exception {
        String pathString = "/json/follow-create-with-id-request.json";
        Path path = Paths.get(Objects.requireNonNull(ITestFollowController.class.getResource(pathString)).toURI());
        String followDTOJson = Files.readString(path);
        String jsonContent = getJsonStringFile("/json/error/business/PreconditionFailed_FollowDTOIdIsNotNull_resp_500.json");
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(followDTOJson))
                .andDo(print())
                .andExpectAll(
                        status().isInternalServerError(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test53_givenWithSameFollowingIdAndFollowerId_thenError_createFollow() throws Exception {
        Long sameUserId = 999L;
        FollowDTO followDTO = new FollowDTO(sameUserId, sameUserId);
        String jsonContent = String.format(
                getJsonStringFile("/json/error/business/PreconditionFailed_SameFollowingAndFollower_resp_500.json"),
                sameUserId,
                sameUserId
        );
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(followDTO)))
                .andDo(print())
                .andExpectAll(
                        status().isInternalServerError(),
                        content().json(jsonContent)
                )
        ;
    }

    @Test
    void test54_givenExist_thenError_createFollow() throws Exception {
        Follow existFollow = follows.get(0);
        FollowDTO followDTO = toFollowDTO(existFollow);
        followDTO.setId(null);
        followDTO.setFollowedAt(null);
        String jsonContent = String.format(
                getJsonStringFile("/json/error/business/PreconditionFailed_FollowAlreadyExists_resp_500.json"),
                RESOURCE,
                followDTO.getFollowingId(),
                followDTO.getFollowerId()
        );
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(followDTO)))
                .andDo(print())
                .andExpectAll(
                        status().isInternalServerError(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test61_givenExist_thenCorrect_deleteFollow() throws Exception {
        Follow followToDelete = follows.get(0);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", followToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String expected = String.format("User(id: %s) has been followed out from User(id: %s). The Follow(id: %s) has been deleted",
                followToDelete.getFollowingId(), followToDelete.getFollowerId(), followToDelete.getId());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);

        Optional<Follow> deletedFollowOpt = followRepository.findById(followToDelete.getId());
        assertFalse(deletedFollowOpt.isPresent());
    }

    @Test
    void test62_givenNotExist_thenCorrect_deleteFollow() throws Exception {
        Long notExistFollowId = 999L;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", notExistFollowId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String expected = String.format("There is no Follow to delete with id: %s", notExistFollowId);
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    private void storeUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    private void storeFollow(Follow follow) {
        entityManager.persist(follow);
        entityManager.flush();
    }
}