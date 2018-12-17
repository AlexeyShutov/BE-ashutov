package com.scloud.util;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class ResponseUtils {

    private ResponseUtils() {}

    public static <T> List<T> responseArrayToList(ResponseEntity<T[]> responseArray) {
        return Optional.ofNullable(responseArray)
                .map(ResponseEntity::getBody)
                .map(List::of)
                .orElse(List.of());
    }
}
