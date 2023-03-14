package com.example.microservices.users.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
//@Entity //TODO Add to liquibase
//@Table(name = "errors")
public class Error {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

//    @Column(name = "http_status")
    private int httpStatus;

//    @Column(name = "frontend_code")
    private String frontendCode;

//    @Column(name = "detail")
    private String detail;

//    @Column(name = "title")
    private String title;

//    @Embedded
//    @AttributeOverrides({
//        @AttributeOverride(name = "jExceptionMsg", column = @Column(name = "meta_exception_msg"))
//    })
    private ErrorMeta meta;
}
