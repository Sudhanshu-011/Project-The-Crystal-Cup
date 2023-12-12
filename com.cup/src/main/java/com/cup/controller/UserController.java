package com.cup.controller;

import com.cup.Security.CustomUserDetailService;
import com.cup.Wrapper.UserWrapper;
import com.cup.entities.User;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserController {

    // All APIs of User
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    // 3) Goes to userControllerImpl
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap);

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken();

    @PostMapping("/changePassword")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);

    @GetMapping("/current-user")
    public User getCurrentUser(Principal principal);
}
