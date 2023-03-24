package com.example.microservices.users.dictionary;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserControllerDictionary {

    public static final String USER_1 = "{\"id\":1,\"firstName\":\"Yury\",\"lastName\":\"Petrov\",\"secondName\":\"Stakanych\",\"gender\":\"MALE\",\"birthday\":\"2000-11-22T00:00:00.000+00:00\",\"currentCity\":{\"id\":1,\"name\":\"Moscow\"},\"nickname\":\"iuric\",\"email\":\"y.petrov@mail.com\",\"phone\":\"+7(999)123-4567\",\"followingsNumber\":2,\"followersNumber\":1}";
    public static final String USER_2 = "{\"id\":2,\"firstName\":\"Anna\",\"lastName\":\"Smile\",\"secondName\":\"Maria\",\"gender\":\"FEMALE\",\"birthday\":\"2002-01-02T00:00:00.000+00:00\",\"currentCity\":{\"id\":4,\"name\":\"Paris\"},\"nickname\":\"asmile\",\"followingsNumber\":1,\"followersNumber\":1}";
    public static final String USER_4 = "{\"id\":4,\"firstName\":\"Petia\",\"lastName\":\"Homo\",\"gender\":\"OTHER\",\"birthday\":\"0001-01-01T00:00:00.000+00:00\",\"currentCity\":{\"id\":2,\"name\":\"Samara\"},\"nickname\":\"phomo\",\"followingsNumber\":0,\"followersNumber\":1}";
    public static final String EXAMPLE_RESPONSE_GET_ALL_OK_200 = "[" + USER_1 + "," + USER_2 + "," + USER_4 + "]";
    public static final String EXAMPLE_RESPONSE_GET_USER_OK_200 = USER_1;
    public static final String EXAMPLE_RESPONSE_GET_USER_NOT_FOUND_ERROR_500 = "{\"errors\":[{\"httpStatusCode\":500,\"frontendCode\":\"businessError\",\"messageToCustomer\":\"Внутренняя бизнес ошибка\",\"meta\":{\"jexceptionMsg\":\"com.example.microservices.users.error.exception.UserNotFoundResponseStatusException{HttpStatus: 404 NOT_FOUND, Reason: Resource User(id: 9999) Not Found}\"}}]}";
    public static final String EXAMPLE_REQUEST_BODY_UPDATE_USER = USER_1;
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_OK_200 = "User(id: 5, nickname: othic) has been updated successfully";
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_PRECONDITION_FAILED_ERROR_500 = "{\"errors\":[{\"httpStatusCode\":500,\"frontendCode\":\"businessError\",\"messageToCustomer\":\"Внутренняя бизнес ошибка\",\"meta\":{\"jexceptionMsg\":\"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: Values of User.id(111) And id(222) argument Are Not Equal}\"}}]}";
    public static final String EXAMPLE_REQUEST_BODY_CREATE_USER = "{\"firstName\":\"Ben\",\"lastName\":\"Mask\",\"gender\":\"MALE\",\"birthday\": \"1990-01-02\",\"currentCity\":{\"id\":5,\"name\":\"Tula\"},\"nickname\":\"benmask\"}";
    public static final String EXAMPLE_RESPONSE_CREATE_USER_OK_200 = "{\"id\":8,\"firstName\":\"Ben\",\"lastName\":\"Mask\",\"gender\":\"MALE\",\"birthday\":\"1990-01-02T00:00:00.000+00:00\",\"currentCity\":{\"id\": 5,\"name\":\"Tula\"},\"nickname\":\"benmask\",\"followingsNumber\":0,\"followersNumber\":0}";
    public static final String EXAMPLE_RESPONSE_CREATE_USER_PRECONDITION_FAILED_ERROR_500 = "{\"errors\":[{\"httpStatusCode\":500,\"frontendCode\":\"businessError\",\"messageToCustomer\":\"Внутренняя бизнес ошибка\",\"meta\":{\"jexceptionMsg\":\"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: User(nickname: usednickname) Already Exists Including Deleted}\"}}]}";
    public static final String EXAMPLE_RESPONSE_DELETE_USER_OK_200 = "User(id: 5, nickname: othic) has been deleted";
    public static final String EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_1_ERROR_500 = "{\"errors\":[{\"httpStatusCode\":500,\"frontendCode\":\"businessError\",\"messageToCustomer\":\"Внутренняя бизнес ошибка\",\"meta\":{\"jexceptionMsg\":\"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: User(id: 9999) Does Not Exist}\"}}]}";
    public static final String EXAMPLE_RESPONSE_DELETE_USER_PRECONDITION_FAILED_2_ERROR_500 = "{\"errors\":[{\"httpStatusCode\":500,\"frontendCode\":\"businessError\",\"messageToCustomer\":\"Внутренняя бизнес ошибка\",\"meta\":{\"jexceptionMsg\":\"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: User(id: 111) Is Already Deleted}\"}}]}";
}
