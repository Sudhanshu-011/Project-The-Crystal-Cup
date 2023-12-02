package com.cup.serviceImpl;

import com.cup.Constants.CupConstants;
import com.cup.Security.CustomUserDetailService;
import com.cup.Security.JwtAuthenticationFilter;
import com.cup.Security.JwtUtils;
import com.cup.Utils.CupUtils;
import com.cup.Utils.EmailUtils;
import com.cup.Wrapper.UserWrapper;
import com.cup.entities.User;
import com.cup.repositories.UserRepo;
import com.cup.service.UserService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private JwtUtils jwtUtils;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private EmailUtils emailUtils;

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

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (this.filter.isAdmin()) {
                return new ResponseEntity<>(this.userRepo.getAllUser(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(this.filter.isAdmin()) {
                Optional<User> optionalUser = this.userRepo.findById(Integer.parseInt(requestMap.get("id")));
                if (optionalUser.isPresent()) {
                    this.userRepo.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    log.info("Initiating mailing process...");
                    // List<String> allAdmin = this.userRepo.getAllAdmin();
                    // allAdmin.add(optionalUser.get().getEmail());
                    this.sendMailToAllAdmin(requestMap.get("status"), optionalUser.get().getEmail(), this.userRepo.getAllAdmin());
                    System.out.println("List of all admins: "+ this.userRepo.getAllAdmin());
                    return CupUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                }
                else return CupUtils.getResponseEntity("User not found.", HttpStatus.OK);
            }
            else {
                return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        log.info("Inside sendMailToAdmin method");
        // this is done because we will pass it to the recipient mail parameter of the method 'sendSimpleMail' and
        // it is also present in all admin list, so we don't want to send the main twice to the current admin.
        allAdmin.remove(this.filter.getCurrentUser());
        log.info("Got all admins");
        if (status != null && status.equalsIgnoreCase("true")) {
            log.info("Inside the if (true) statement in this method");
            this.emailUtils.sendSimpleMail(this.filter.getCurrentUser(), "Account Approved", "USER:- " + user +"\nis approved by:-" + "\nADMIN:- "+ this.filter.getCurrentUser(), allAdmin);
        }
        else {
            log.info("Inside the else statement (false) statement in this method");
            this.emailUtils.sendSimpleMail(this.filter.getCurrentUser(), "Account Disabled", "USER:-" + user +"\nis Disabled by:-" + "\nADMIN:-"+ this.filter.getCurrentUser(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CupUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = this.userRepo.findByEmail(this.filter.getCurrentUser());
            if (!Objects.isNull(userObj)) {
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    this.userRepo.save(userObj);
                    System.out.println("Password changed Successfully");
                    return new ResponseEntity<>("{\"message\":\"Password changed successfully\"}", HttpStatus.OK);
                }
                else {
                    return CupUtils.getResponseEntity("Incorrect old password.", HttpStatus.OK);
                }
            }
            return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = this.userRepo.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                this.emailUtils.forgotMail(user.getEmail(), "Credentials by The Crystal Cup", user.getPassword());
            }
            return CupUtils.getResponseEntity("Check email for Credentials", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
