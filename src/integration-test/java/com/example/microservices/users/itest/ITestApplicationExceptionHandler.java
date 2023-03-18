package com.example.microservices.users.itest;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.microservices.users.util.ITestUtils.getJsonStringFile;
import static com.example.microservices.users.util.MapperTestUtils.initMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class ITestApplicationExceptionHandler {

    @Container private static final PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
    private static final ObjectMapper mapper = initMapper();
    private @Autowired MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {

    }

    //TODO This is a draft
    @Ignore
    @Test
    void test101_given_UnSupportedRequestMethod_HttpRequestMethodNotSupportedException() throws Exception {
//        User userToCreate = testUsers.get(0);
//        userToCreate.setDeleted(true);
//        updateUser(userToCreate);
//
//        UserDTO userDTO = toUserDTO(userToCreate);
//        String requestBody = mapper.writeValueAsString(userDTO);
        String requestBody = StringUtils.EMPTY;
        MediaType unSupportedMediaType = MediaType.TEXT_PLAIN;
        String jsonContent = getJsonStringFile("/json/error/HttpMediaTypeNotSupportedException_resp_body.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(unSupportedMediaType).content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        status().isUnsupportedMediaType(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test102_given_UnSupportedMediaType_HttpMediaTypeNotSupportedException() throws Exception {
        String requestBody = StringUtils.EMPTY;
        MediaType unSupportedMediaType = MediaType.TEXT_PLAIN;
        String jsonContent = getJsonStringFile("/json/error/HttpMediaTypeNotSupportedException_resp_body.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(unSupportedMediaType).content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        status().isUnsupportedMediaType(),
                        content().json(jsonContent)
                );
    }
}

// HttpMediaTypeNotAcceptableException.class, //103
// MissingPathVariableException.class, //104
// MissingServletRequestParameterException.class, //105
// ServletRequestBindingException.class, //106
// ConversionNotSupportedException.class, //107
// TypeMismatchException.class, //108
// HttpMessageNotReadableException.class, //109
// HttpMessageNotWritableException.class, //110
// MethodArgumentNotValidException.class, //111
// MissingServletRequestPartException.class, //112
// BindException.class, //113
// NoHandlerFoundException.class, //114
// AsyncRequestTimeoutException.class. //115

//TODO FIX
//[ERROR] Failures:
//[ERROR]   ITestFollowController.test42_givenNotExistFollowId_thenError_getFollow:166 expected: <404> but was: <500>
//[ERROR]   ITestFollowController.test52_givenWithId_thenError_createFollow:198 expected: <412> but was: <500>
//[ERROR]   ITestFollowController.test53_givenWithSameFollowingIdAndFollowerId_thenError_createFollow:211 expected: <412> but was: <500>
//[ERROR]   ITestFollowController.test54_givenExist_thenError_createFollow:224 expected: <412> but was: <500>
//[ERROR]   ITestUserController.test22_givenNotExistUserId_thenError_getUser:127 expected: <404> but was: <500>
//[ERROR]   ITestUserController.test32_givenNotExistUpdatedUser_thenError_updateUser:154 expected: <422> but was: <500>
//[ERROR]   ITestUserController.test42_givenExistAndUndeletedUser_thenError_createUser:176 expected: <412> but was: <500>
//[ERROR]   ITestUserController.test43_givenExistAndDeletedUser_thenError_createUser:190 expected: <412> but was: <500>
//[ERROR]   ITestUserController.test52_givenNotExistUser_thenError_deleteUser:233 expected: <412> but was: <500>
//[ERROR]   ITestUserController.test53_givenExistAndDeletedUser_thenError_deleteUser:246 expected: <412> but was: <500>