package com.example.microservices.users.service;

import com.example.microservices.users.entity.Follow;
import com.example.microservices.users.repository.FollowRepository;
import com.example.microservices.users.util.FollowTestUtils.TestFollow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.microservices.users.util.FollowTestUtils.fillUpTestFollows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class FollowServiceTest {
    private static final int TEST_LIST_SIZE = 10;

    private final FollowRepository repository = mock(FollowRepository.class);
    private final FollowService service = new FollowService(repository);
    private final List<Follow> testFollows = new ArrayList<>();

    @BeforeEach
    void setUp() {
        fillUpTestFollows(TEST_LIST_SIZE, testFollows);
    }

    @AfterEach
    void tearDown() {
        testFollows.clear();
    }

    @Test
    void test1_thenCorrect_getAll() {
        List<Follow> expectedList = testFollows;
        when(repository.findAll()).thenReturn(expectedList);
        List<Follow> actualList = service.getAll();
        assertIterableEquals(expectedList, actualList);
    }

    @Test
    void test2_givenFollowerId_thenCorrect_getAllFollowings() {
        Long followerId = testFollows.get(0).getFollowerId();
        List<Follow> expectedList = testFollows.stream().filter(follow -> Objects.equals(follow.getFollowerId(), followerId)).collect(Collectors.toList());
        when(repository.findAllByFollowerId(followerId)).thenReturn(expectedList);
        List<Follow> actualList = service.getAllFollowings(followerId);
        assertIterableEquals(expectedList, actualList);
    }

    @Test
    void test3_givenFollowingId_thenCorrect_getAllFollowers() {
        Long followingId = testFollows.get(0).getFollowerId();
        List<Follow> expectedList = testFollows.stream().filter(follow -> Objects.equals(follow.getFollowingId(), followingId)).collect(Collectors.toList());
        when(repository.findAllByFollowingId(followingId)).thenReturn(expectedList);
        List<Follow> actualList = service.getAllFollowings(followingId);
        assertIterableEquals(expectedList, actualList);
    }

    @Test
    void test41_givenExistFollowId_thenCorrect_getFollow() {
        Follow expected = testFollows.get(0);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Follow actual = service.getFollow(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void test42_givenNotExistFollowId_thenError_getFollow() {
        Follow follow = new TestFollow(2L * testFollows.size(), 1L, 2L);
        HttpStatus expectedHttpStatus = HttpStatus.NOT_FOUND;
        when(repository.findById(follow.getId())).thenThrow(new ResponseStatusException(expectedHttpStatus));
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.getFollow(follow.getId());
            }
            catch (ResponseStatusException e) {
                actualHttpStatus[0] = e.getStatus();
                throw e;
            }
        };
        assertThrows(ResponseStatusException.class, actual);
        assertEquals(expectedHttpStatus, actualHttpStatus[0]);
    }

    @Test
    void test51_givenNew_thenCorrect_createFollow() {
        Follow followToSave = new Follow(95L, 910L);
        Follow savedFollow = new TestFollow(1000L, followToSave.getFollowingId(), followToSave.getFollowerId());
        when(repository.findByFollowingIdAndFollowerId(followToSave.getFollowingId(), followToSave.getFollowerId()))
                .thenReturn(Optional.empty());
        when(repository.save(followToSave)).thenReturn(savedFollow);
        String expected = String.format("User(id: %s) has been followed to User(id: %s) with Follow(id: %s)",
                savedFollow.getFollowingId(), savedFollow.getFollowerId(), savedFollow.getId());
        String actual = service.createFollow(followToSave);
        assertEquals(expected, actual);
    }

    @Test
    void test53_givenWithSameFollowingIdAndFollowerId_thenError_createFollow() {
        Long sameId = 1L;
        Follow followToSave = new Follow(sameId, sameId);
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.createFollow(followToSave);
            }
            catch (ResponseStatusException e) {
                actualHttpStatus[0] = e.getStatus();
                throw e;
            }
        };
        assertThrows(ResponseStatusException.class, actual);
        assertEquals(expectedHttpStatus, actualHttpStatus[0]);
    }

    @Test
    void test54_givenExist_thenError_createFollow() {
        Follow followToSave = new Follow(95L, 910L);
        Follow savedFollow = new TestFollow(1000L, followToSave.getFollowingId(), followToSave.getFollowerId());
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        when(repository.findByFollowingIdAndFollowerId(followToSave.getFollowingId(), followToSave.getFollowerId()))
                .thenReturn(Optional.of(savedFollow));
        when(repository.save(followToSave)).thenReturn(savedFollow);
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.createFollow(followToSave);
            }
            catch (ResponseStatusException e) {
                actualHttpStatus[0] = e.getStatus();
                throw e;
            }
        };
        assertThrows(ResponseStatusException.class, actual);
        assertEquals(expectedHttpStatus, actualHttpStatus[0]);
    }

    @Test
    void test61_givenExist_thenCorrect_deleteFollow() {
        Follow followToDelete = testFollows.get(0);
        when(repository.findById(followToDelete.getId())).thenReturn(Optional.of(followToDelete));
        doNothing().when(repository).deleteById(followToDelete.getId());
        String expected = String.format("User(id: %s) has been followed out from User(id: %s). The Follow(id: %s) has been deleted",
                followToDelete.getFollowingId(), followToDelete.getFollowerId(), followToDelete.getId());
        String actual = service.deleteFollow(followToDelete.getId());
        assertEquals(expected, actual);
    }

    @Test
    void test62_givenNotExist_thenCorrect_deleteFollow() {
        Long notExistFollowId = 999L;
        when(repository.findById(notExistFollowId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(notExistFollowId);
        String expected = String.format("There is no Follow to delete with id: %s", notExistFollowId);
        String actual = service.deleteFollow(notExistFollowId);
        assertEquals(expected, actual);
    }

    @Test
    void test7_givenUserIdAndTrue_thenCorrect_setRefersDeletedUserInAllWhereFollowingIdOrFollowerId() {
        Long userId = 1L;
        boolean flag = true;
        doNothing().when(repository).setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(userId, flag);
        service.setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(userId, flag);
    }
}