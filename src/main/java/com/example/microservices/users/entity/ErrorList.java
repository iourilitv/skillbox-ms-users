package com.example.microservices.users.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO Replace with JpaRepository
@Data
@Accessors(chain = true)
public class ErrorList {

    private final List<Error> errors;

    public ErrorList() {
        this.errors = new ArrayList<>();
    }

    public ErrorList(Error... errors) {
        this.errors = Arrays.asList(errors);
    }

    public ErrorList(List<Error> errors) {
        this.errors = errors;
    }

    public void add(Error error) {
        errors.add(error);
    }
}
