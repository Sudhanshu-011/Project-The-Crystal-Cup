package com.cup.Security;

import com.cup.entities.User;
import com.cup.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    private User userDetails;

    // 6) from here control goes to userRepo findByEmailId(email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername: {}", email);
        userDetails = this.userRepo.findByEmailId(email);
        if (!Objects.isNull(userDetails)) {
            log.info("User was found {}", userDetails.getEmail());
            return new org.springframework.security.core.userdetails.User(userDetails.getEmail(), userDetails.getPassword(), new ArrayList<>());
        }
        else throw new UsernameNotFoundException("User not found.");
    }

    // When we need to return complete user detail if needed
    public User getUserDetails() {
        return userDetails;
    }
}
