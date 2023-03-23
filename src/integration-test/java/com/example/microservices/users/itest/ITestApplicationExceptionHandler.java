package com.example.microservices.users.itest;

import com.example.microservices.users.UsersApplication;
import com.example.microservices.users.util.ITestUtilPostgreSQLContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.microservices.users.util.ITestUtils.getJsonStringFile;
import static com.example.microservices.users.util.MapperTestUtils.initMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private static final String BASE_URL = "/users";

    @Container private static final PostgreSQLContainer<?> sqlContainer = ITestUtilPostgreSQLContainer.getInstance();
    private static final ObjectMapper mapper = initMapper();
    private @Autowired MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test101_given_UnSupportedRequestMethod_HttpRequestMethodNotSupportedException() throws Exception {
        String jsonContent = getJsonStringFile("/json/error/HttpRequestMethodNotSupportedException_resp_body.json");
        mockMvc.perform(delete(BASE_URL))
                .andDo(print())
                .andExpectAll(
                        status().isMethodNotAllowed(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test102_given_UnSupportedMediaType_HttpMediaTypeNotSupportedException() throws Exception {
        MediaType unSupportedMediaType = MediaType.TEXT_PLAIN;
        String requestBody = StringUtils.EMPTY;
        String jsonContent = getJsonStringFile("/json/error/HttpMediaTypeNotSupportedException_resp_body.json");
        mockMvc.perform(post(BASE_URL).contentType(unSupportedMediaType).content(requestBody))
                .andDo(print())
                .andExpectAll(
                        status().isUnsupportedMediaType(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test109_given_NoBody_HttpMessageNotReadableException() throws Exception {
        String urlTemplate = String.format(BASE_URL + "/%s", 1L);
        String requestBody = StringUtils.EMPTY;
        String jsonContent = getJsonStringFile("/json/error/HttpMessageNotReadableException_resp_body.json");
        mockMvc.perform(put(urlTemplate).content(requestBody))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().json(jsonContent)
                );
    }

    @Test
    void test901_given_wrongPathVariableType_MethodArgumentTypeMismatchException() throws Exception {
        String wrongPathVariableType = "wrongPathVariableType";
        String urlTemplate = BASE_URL + "/" + wrongPathVariableType;
        String jsonContent = getJsonStringFile("/json/error/MethodArgumentTypeMismatchException_resp_body.json");
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().json(jsonContent)
                );
    }
}

// HttpMediaTypeNotAcceptableException.class, //103
// MissingPathVariableException.class, //104 //Can't make an imitation
// MissingServletRequestParameterException.class, //105
// ServletRequestBindingException.class, //106
// ConversionNotSupportedException.class, //107
// TypeMismatchException.class, //108
// HttpMessageNotWritableException.class, //110
// MethodArgumentNotValidException.class, //111
// MissingServletRequestPartException.class, //112
// BindException.class, //113
// NoHandlerFoundException.class, //114
// AsyncRequestTimeoutException.class. //115