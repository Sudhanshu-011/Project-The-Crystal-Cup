package com.cup.controllerImpl;

import com.cup.Constants.CupConstants;
import com.cup.Security.CustomUserDetailService;
import com.cup.Security.JwtAuthenticationFilter;
import com.cup.Utils.CupUtils;
import com.cup.Wrapper.UserWrapper;
import com.cup.controller.UserController;
import com.cup.entities.User;
import com.cup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailService service;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
    }

    // 3) goes to UserService
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return this.userService.getAllUsers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return this.userService.update(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return this.userService.checkToken();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updatePassword(Map<String, String> requestMap) {
        try {
            return this.userService.changePassword(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return this.userService.forgotPassword(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User getCurrentUser(Principal principal)
        {
            System.out.println(this.filter.getCurrentUser());
            return (User)this.service.loadUserByUsername(this.filter.getCurrentUser());
        }

}
