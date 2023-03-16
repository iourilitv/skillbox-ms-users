package com.example.microservices.users.error.helpers;

import lombok.experimental.UtilityClass;

/**
 * Messages that will be written in log on exceptions.
 */
@UtilityClass
public class ErrorMessage {

    public static final String HANDLING_ERROR_MESSAGE = "Handling error...";
    public static final String VALIDATION_ERROR_MESSAGE = "Some of input parameters are incorrect";
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error has occurred";
}
