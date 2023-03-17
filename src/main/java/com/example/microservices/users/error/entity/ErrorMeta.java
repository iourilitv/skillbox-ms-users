package com.example.microservices.users.error.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorMeta {

    private String jExceptionMsg;

    public static ErrorMeta fromException(Exception ex) {
        return new ErrorMeta().setJExceptionMsg(ex.toString());
    }
}
