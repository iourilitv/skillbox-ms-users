package com.example.microservices.users.error.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Error {

    private int httpStatusCode;
    private String frontendCode;
    private String messageToCustomer;
    private ErrorMeta meta;
}
