package com.example.microservices.users.controller;

import com.example.microservices.users.dto.FollowDTO;
import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.mapper.FollowMapper;
import com.example.microservices.users.service.FollowService;
import com.example.microservices.users.util.FollowTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.FollowTestUtils.createFollowDTOs;
import static com.example.microservices.users.util.FollowTestUtils.toFollow;
import static com.example.microservices.users.util.MapperTestUtils.initMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(FollowController.class)
@TestMethodOrder(value = MethodOrderer.MethodName.class)
class FollowControllerTest {
    private static final int TEST_FOLLOWS_SIZE = 10;
    private static final String BASE_URL = "/follows";

    private static final ObjectMapper mapper = initMapper();
    @Autowired private MockMvc mockMvc;

    @MockBean private FollowMapper followMapper;
    @MockBean private FollowService followService;

    private List<FollowDTO> followDTOs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        followDTOs = createFollowDTOs(TEST_FOLLOWS_SIZE);
    }

    @AfterEach
    void tearDown() {
        followDTOs.clear();
    }

    @Test
    void test1_thenCorrect_getAll() throws Exception {
        List<Follow> follows = followDTOs.stream().map(FollowTestUtils::toFollow).collect(Collectors.toList());
        when(followService.getAll()).thenReturn(follows);
        when(followMapper.toDTOList(follows)).thenReturn(followDTOs);

        String expectedJson = mapper.writeValueAsString(followDTOs);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test2_givenFollowerId_thenCorrect_getAllFollowings() throws Exception {
        long followerId = followDTOs.get(0).getFollowerId();
        List<FollowDTO> followingsDTOs = followDTOs.stream()
                .filter(followDTO -> Objects.equals(followDTO.getFollowerId(), followerId)).collect(Collectors.toList());
        List<Follow> followings = followingsDTOs.stream().map(FollowTestUtils::toFollow).collect(Collectors.toList());
        when(followService.getAllFollowings(followerId)).thenReturn(followings);
        when(followMapper.toDTOList(followings)).thenReturn(followingsDTOs);

        String expectedJson = mapper.writeValueAsString(followingsDTOs);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/followings/{followerId}", followerId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test3_givenFollowingId_thenCorrect_getAllFollowers() throws Exception {
        long followingId = followDTOs.get(0).getFollowingId();
        List<FollowDTO> followersDTOs = followDTOs.stream()
                .filter(followDTO -> Objects.equals(followDTO.getFollowingId(), followingId)).collect(Collectors.toList());
        List<Follow> followers = followersDTOs.stream().map(FollowTestUtils::toFollow).collect(Collectors.toList());
        when(followService.getAllFollowers(followingId)).thenReturn(followers);
        when(followMapper.toDTOList(followers)).thenReturn(followersDTOs);

        String expectedJson = mapper.writeValueAsString(followersDTOs);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/followers/{followingId}", followingId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test41_givenExistFollowId_thenCorrect_getFollow() throws Exception {
        FollowDTO expected = followDTOs.get(0);
        Follow follow = toFollow(expected);
        when(followService.getFollow(expected.getId())).thenReturn(follow);
        when(followMapper.toDTO(follow)).thenReturn(expected);

        String expectedJson = mapper.writeValueAsString(expected);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", expected.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test51_givenNew_thenCorrect_createFollow() throws Exception {
        FollowDTO followDTO = followDTOs.get(1);
        FollowDTO dtoToCreate = new FollowDTO(followDTO.getFollowingId(), followDTO.getFollowerId());
        Follow followToCreate = toFollow(dtoToCreate);
        when(followMapper.toEntity(dtoToCreate)).thenReturn(followToCreate);
        FollowDTO expectedDTO =  new FollowDTO(followDTO.getFollowingId(), followDTO.getFollowerId());
        expectedDTO.setId(99L);
        expectedDTO.setFollowedAt(new Date());
        Follow follow = toFollow(expectedDTO);
        when(followService.createFollow(followToCreate)).thenReturn(follow);
        when(followMapper.toDTO(follow)).thenReturn(expectedDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dtoToCreate))).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String expectedJson = mapper.writeValueAsString(expectedDTO);
        String actualJson = response.getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void test61_givenExist_thenCorrect_deleteFollow() throws Exception {
        FollowDTO dtoToDelete = followDTOs.get(0);
        String expected = String.format("User(id: %s) has been followed out from User(id: %s). The Follow(id: %s) has been deleted",
                dtoToDelete.getFollowingId(), dtoToDelete.getFollowerId(), dtoToDelete.getId());
        when(followService.deleteFollow(dtoToDelete.getId())).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", dtoToDelete.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }
}
