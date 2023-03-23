package com.example.microservices.users.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ITestUtils {

    public static String getJsonStringFile(String pathString) {
        String jsonString = null;
        try {
            Path path = Paths.get(Objects.requireNonNull(ITestUtils.class.getResource(pathString)).toURI());
            jsonString = Files.readString(path);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private ITestUtils() {
        throw new RuntimeException("Utility class");
    }
}
