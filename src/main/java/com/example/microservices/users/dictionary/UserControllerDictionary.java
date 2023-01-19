package com.example.microservices.users.dictionary;

public class UserControllerDictionary {
    public static final String EXAMPLE_RESPONSE_GET_ALL_OK_200 = "[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"firstName\": \"Yury\",\n" +
            "    \"lastName\": \"Petrov\",\n" +
            "    \"secondName\": \"Stakanych\",\n" +
            "    \"gender\": \"MALE\",\n" +
            "    \"birthday\": \"2000-11-22T00:00:00.000+00:00\",\n" +
            "    \"currentCity\": {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Moscow\"\n" +
            "    },\n" +
            "    \"nickname\": \"iuric\",\n" +
            "    \"email\": \"y.petrov@mail.com\",\n" +
            "    \"phone\": \"+7(999)123-4567\",\n" +
            "    \"followingsNumber\": 2,\n" +
            "    \"followersNumber\": 1\n" +
            "  },\n" +
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
    public static final String EXAMPLE_RESPONSE_GET_USER_OK_200 = "{\n" +
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
    public static final String EXAMPLE_RESPONSE_GET_USER_ERROR_404 = "{\n" +
            "  \"timestamp\": \"2022-12-06T21:47:11.775+00:00\",\n" +
            "  \"status\": 404,\n" +
            "  \"error\": \"Not Found\",\n" +
            "  \"path\": \"/users/5\"\n" +
            "}";

    public static final String EXAMPLE_REQUEST_BODY_UPDATE_USER = "{\n" +
            "  \"id\": 1,\n" +
            "  \"firstName\": \"Yury\",\n" +
            "  \"lastName\": \"Petrov\",\n" +
            "  \"secondName\": \"UpdatedStakanych\",\n" +
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
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_OK_200 = "User(id: 5, nickname: othic) has been updated successfully";
    public static final String EXAMPLE_RESPONSE_UPDATE_USER_ERROR_422 = "{\n" +
            "  \"timestamp\": \"2022-12-07T05:43:37.995+00:00\",\n" +
            "  \"status\": 422,\n" +
            "  \"error\": \"Unprocessable Entity\",\n" +
            "  \"path\": \"/users/10\"\n" +
            "}";

    public static final String EXAMPLE_REQUEST_BODY_CREATE_USER = "{\n" +
            "  \"firstName\": \"Oth\",\n" +
            "  \"lastName\": \"Other\",\n" +
            "  \"gender\": \"OTHER\",\n" +
            "  \"birthday\": \"1990-05-12\",\n" +
            "  \"currentCity\": {\n" +
            "    \"id\": 4,\n" +
            "    \"name\": \"Paris\"\n" +
            "  },\n" +
            "  \"nickname\": \"othic\"\n" +
            "}";
    public static final String EXAMPLE_RESPONSE_CREATE_USER_OK_200 = "New user(nickname: othic) has been saved with id: 5";
    public static final String EXAMPLE_RESPONSE_CREATE_USER_ERROR_412 = "{\n" +
            "  \"timestamp\": \"2022-12-06T21:52:23.801+00:00\",\n" +
            "  \"status\": 412,\n" +
            "  \"error\": \"Precondition Failed\",\n" +
            "  \"path\": \"/users\"\n" +
            "}";

    public static final String EXAMPLE_RESPONSE_DELETE_USER_OK_200 = "User(id: 5, nickname: othic) has been deleted";
    public static final String EXAMPLE_RESPONSE_DELETE_USER_ERROR_412 = "{\n" +
            "  \"timestamp\": \"2022-12-06T21:52:23.801+00:00\",\n" +
            "  \"status\": 412,\n" +
            "  \"error\": \"Precondition Failed\",\n" +
            "  \"path\": \"/users/99999\"\n" +
            "}";
}
