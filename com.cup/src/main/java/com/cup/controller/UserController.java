package com.cup.controller;

import com.cup.Wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserController {

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    // 3) Goes to userControllerImpl
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();
}
