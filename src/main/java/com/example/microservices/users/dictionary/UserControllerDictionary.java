package com.example.microservices.users.dictionary;

public class UserControllerDictionary {
    private static final String USER_1 =
            "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"firstName\": \"Yury\",\n" +
                    "  \"lastName\": \"Petrov\",\n" +
                    "  \"secondName\": \"Stakanych\",\n" +
                    "  \"gender\": \"MALE\",\n" +
                    "  \"birthday\": \"2000-11-22T00:00:00.000+00:00\",\n" +
                    "  \"currentCity\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"Moscow\"\n" +
                    "  },\n" +
                    "  \"nickname\": \"iuric\",\n" +
                    "  \"email\": \"y.petrov@mail.com\",\n" +
                    "  \"phone\": \"+7(999)123-4567\",\n" +
                    "  \"followingsNumber\": 2,\n" +
                    "  \"followersNumber\": 1\n" +
                    "}";

    public static final String EXAMPLE_RESPONSE_GET_ALL_OK_200 = "[\n" +
            USER_1 + ",\n" +
            "  {\n" +
            "    \"id\": 2,\n" +
            "    \"firstName\": \"Anna\",\n" +
            "    \"lastName\": \"Smile\",\n" +
            "    \"secondName\": \"Maria\",\n" +
            "    \"gender\": \"FEMALE\",\n" +
            "    \"birthday\": \"2002-01-02T00:00:00.000+00:00\",\n" +
            "    \"currentCity\": {\n" +
            "      \"id\": 4,\n" +
            "      \"name\": \"Paris\"\n" +
            "    },\n" +
            "    \"nickname\": \"asmile\",\n" +
            "    \"followingsNumber\": 1,\n" +
            "    \"followersNumber\": 1\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 4,\n" +
            "    \"firstName\": \"Petia\",\n" +
            "    \"lastName\": \"Homo\",\n" +
            "    \"gender\": \"OTHER\",\n" +
            "    \"birthday\": \"0001-01-01T00:00:00.000+00:00\",\n" +
            "    \"currentCity\": {\n" +
            "      \"id\": 2,\n" +
            "      \"name\": \"Samara\"\n" +
            "    },\n" +
            "    \"nickname\": \"phomo\",\n" +
            "    \"followingsNumber\": 0,\n" +
            "    \"followersNumber\": 1\n" +
            "  }\n" +
            "]";
    public static final String EXAMPLE_RESPONSE_GET_USER_OK_200 = USER_1;
    public static final String EXAMPLE_RESPONSE_GET_USER_NOT_FOUND_ERROR_500 = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"httpStatusCode\": 500,\n" +
            "      \"frontendCode\": \"businessError\",\n" +
            "      \"messageToCustomer\": \"Внутренняя бизнес ошибка\",\n" +
            "      \"meta\": {\n" +
            "        \"jexceptionMsg\": \"com.example.microservices.users.error.exception.UserNotFoundResponseStatusException{HttpStatus: 404 NOT_FOUND, Reason: Resource User(id: 9999) Not Found}\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String EXAMPLE_REQUEST_BODY_UPDATE_USER = USER_1;
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_OK_200 = "User(id: 5, nickname: othic) has been updated successfully";
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_PRECONDITION_FAILED_ERROR_500 = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"httpStatusCode\": 500,\n" +
            "      \"frontendCode\": \"businessError\",\n" +
            "      \"messageToCustomer\": \"Внутренняя бизнес ошибка\",\n" +
            "      \"meta\": {\n" +
            "        \"jexceptionMsg\": \"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: Values of User.id(111) And id(222) argument Are Not Equal}\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String EXAMPLE_REQUEST_BODY_CREATE_USER = "{\n" +
            "  \"firstName\": \"Ben\",\n" +
            "  \"lastName\": \"Mask\",\n" +
            "  \"gender\": \"MALE\",\n" +
            "  \"birthday\": \"1990-01-02\",\n" +
            "  \"currentCity\": {\n" +
            "    \"id\": 5,\n" +
            "    \"name\": \"Tula\"\n" +
            "  },\n" +
            "  \"nickname\": \"benmask\"\n" +
            "}";

    public static final String EXAMPLE_RESPONSE_CREATE_USER_OK_200 = "{\n" +
            "    \"id\": 8,\n" +
            "    \"firstName\": \"Ben\",\n" +
            "    \"lastName\": \"Mask\",\n" +
            "    \"gender\": \"MALE\",\n" +
            "    \"birthday\": \"1990-01-02T00:00:00.000+00:00\",\n" +
            "    \"currentCity\": {\n" +
            "        \"id\": 5,\n" +
            "        \"name\": \"Tula\"\n" +
            "    },\n" +
            "    \"nickname\": \"benmask\",\n" +
            "    \"followingsNumber\": 0,\n" +
            "    \"followersNumber\": 0\n" +
            "}";

    public static final String EXAMPLE_RESPONSE_CREATE_USER_PRECONDITION_FAILED_ERROR_500 = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"httpStatusCode\": 500,\n" +
            "      \"frontendCode\": \"businessError\",\n" +
            "      \"messageToCustomer\": \"Внутренняя бизнес ошибка\",\n" +
            "      \"meta\": {\n" +
            "        \"jexceptionMsg\": \"com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: User(nickname: usednickname) Already Exists Including Deleted}\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String EXAMPLE_RESPONSE_DELETE_USER_OK_200 = "User(id: 5, nickname: othic) has been deleted";
    public static final String EXAMPLE_RESPONSE_DELETE_USER_ERROR_412 = "{\n" +
            "  \"timestamp\": \"2022-12-06T21:52:23.801+00:00\",\n" +
            "  \"status\": 412,\n" +
            "  \"error\": \"Precondition Failed\",\n" +
            "  \"path\": \"/users/99999\"\n" +
            "}";

    private UserControllerDictionary() {
        throw new IllegalStateException("Utility class");
    }
}
