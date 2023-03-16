package com.example.microservices.users.error.entity;

import lombok.Data;
import lombok.experimental.Accessors;

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

//    @Column(name = "title")
    private String title;

//    @Column(name = "detail")
    private String detail;

//    @Embedded
//    @AttributeOverrides({
//        @AttributeOverride(name = "jExceptionMsg", column = @Column(name = "meta_exception_msg"))
//    })
    private ErrorMeta meta;
}
