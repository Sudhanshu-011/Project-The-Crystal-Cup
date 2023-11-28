package com.cup.serviceImpl;

import com.cup.Constants.CupConstants;
import com.cup.Security.CustomUserDetailService;
import com.cup.Security.JwtUtils;
import com.cup.Utils.CupUtils;
import com.cup.entities.User;
import com.cup.repositories.UserRepo;
import com.cup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private  JwtUtils jwtUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUpd {}", requestMap);
        try {
            if (this.validateSignUpMap(requestMap)) {
                User user = this.userRepo.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    this.userRepo.save(this.getUserFromMap(requestMap));
                    return CupUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return CupUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CupUtils.getResponseEntity(CupConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (
                requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")
        ) {
            return true;
        }
        else return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();

        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }

    // 5) goes inside try statement and then goes to try statement and executed without exception then goes to if statement
    // then to the CustomUserDetailService loadUserByUsername
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                log.info("Auth was authenticated");
                if (this.customUserDetailService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + this.jwtUtils.generateToken(
                            this.customUserDetailService.getUserDetails().getEmail(),
                            this.customUserDetailService.getUserDetails().getRole()
                    ) + "\"}", HttpStatus.OK);
                }
                else  {
                    return CupUtils.getResponseEntity("Wait for admin approval", HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity("Wrong Credentials", HttpStatus.BAD_REQUEST);
    }
}
