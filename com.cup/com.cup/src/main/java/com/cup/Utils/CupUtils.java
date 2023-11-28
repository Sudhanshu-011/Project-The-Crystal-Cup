package com.cup.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CupUtils {
    private CupUtils() { }

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\""+message+"\"}", httpStatus);
        // this message structure is used to return a JSON object. '\"' is for double quote
    }
}
