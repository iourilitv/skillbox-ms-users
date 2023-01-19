package com.example.microservices.users.service;

import com.example.microservices.users.entity.City;
import com.example.microservices.users.entity.User;
import com.example.microservices.users.enums.Gender;
import com.example.microservices.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.microservices.users.util.UserTestUtils.fillUpTestUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class UserServiceTest {
    private static final int TEST_LIST_SIZE = 10;

    UserRepository repository = mock(UserRepository.class);
    FollowService followService = mock(FollowService.class);
    UserService service = new UserService(repository, followService);
    List<User> testUsers = new ArrayList<>(TEST_LIST_SIZE);

    @BeforeEach
    void setUp() {
        fillUpTestUsers(TEST_LIST_SIZE, testUsers);
    }

    @AfterEach
    void tearDown() {
        testUsers.clear();
    }

    @Test
    void test1_thenCorrect_getAll() {
        when(repository.findAll()).thenReturn(testUsers);
        List<User> actualList = service.getAll();
        assertIterableEquals(testUsers, actualList);
    }

    @Test
    void test21_givenExistUserId_thenCorrect_getUser() {
        User expected = testUsers.get(0);
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        User actual = service.getUser(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void test22_givenNotExistUserId_thenError_getUser() {
        long notExistId = 9999L;
        HttpStatus expectedHttpStatus = HttpStatus.NOT_FOUND;
        when(repository.findById(notExistId)).thenThrow(new ResponseStatusException(expectedHttpStatus));
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.getUser(notExistId);
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
    void test3_givenUpdatedUser_thenCorrect_updateUser() {
        User updatedUser = testUsers.get(0);
        updatedUser.setSecondName("updated " + updatedUser.getSecondName());
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        String expected = String.format("User(id: %s, nickname: %s) has been updated successfully", updatedUser.getId(), updatedUser.getNickname());
        String actual = service.updateUser(updatedUser);
        assertEquals(expected, actual);
    }

    @Test
    void test41_givenNotExistUser_thenCorrect_createUser() {
        City city = new City();
        city.setId(100);
        User user = new User("new first name", "new last name", Gender.FEMALE, new Date(), city, "new_user");
        when(repository.findByNickname(user.getNickname())).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(user);
        String expected = String.format("New user(nickname: %s) has been saved with id: %s", user.getNickname(), user.getId());
        String actual = service.createUser(user);
        assertEquals(expected, actual);
    }

    @Test
    void test42_givenExistUser_thenError_createUser() {
        City city = new City();
        city.setId(100);
        User user = new User("new first name", "new last name", Gender.FEMALE, new Date(), city, "new_user");
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        when(repository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.createUser(user);
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
    void test51_givenExistAndUndeletedUser_thenCorrect_deleteUser() {
        User user = testUsers.get(0);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(repository).delete(user);
        doNothing().when(followService).setRefersDeletedUserInAllWhereFollowingIdOrFollowerId(user.getId(), true);
        String expected = String.format("User(id: %s, nickname: %s) has been deleted", user.getId(), user.getNickname());
        String actual = service.deleteUser(user.getId());
        assertEquals(expected, actual);
    }

    @Test
    void test52_givenNotExistUser_thenError_deleteUser() {
        Long notExistId = 999L;
        when(repository.findById(notExistId)).thenReturn(Optional.empty());
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.deleteUser(notExistId);
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
    void test53_givenExistAndIsDeletedUser_thenError_deleteUser() {
        User isDeletedUser = testUsers.get(0);
        isDeletedUser.setDeleted(true);
        when(repository.findById(isDeletedUser.getId())).thenReturn(Optional.of(isDeletedUser));
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        final HttpStatus[] actualHttpStatus = new HttpStatus[1];
        Executable actual = () -> {
            try {
                service.deleteUser(isDeletedUser.getId());
            }
            catch (ResponseStatusException e) {
                actualHttpStatus[0] = e.getStatus();
                throw e;
            }
        };
        assertThrows(ResponseStatusException.class, actual);
        assertEquals(expectedHttpStatus, actualHttpStatus[0]);
    }
}