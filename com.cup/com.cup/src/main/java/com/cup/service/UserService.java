package com.cup.service;

import org.apache.coyote.Request;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    ResponseEntity<String> signUp(Map<String, String> requestMap);

    // 4) Goes to UserServiceImpl
    ResponseEntity<String> login(Map<String, String> requestMap);
}
