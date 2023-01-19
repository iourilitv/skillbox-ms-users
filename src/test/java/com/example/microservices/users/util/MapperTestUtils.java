package com.example.microservices.users.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.util.TimeZone;

public class MapperTestUtils {

    public static ObjectMapper initMapper() {
        ObjectMapper mapper = new JsonMapper();
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));    //"birthday":"2022-12-02T06:55:59.842+00:00"
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));                        // timestamp in db without time zone
        return mapper;
    }
}
