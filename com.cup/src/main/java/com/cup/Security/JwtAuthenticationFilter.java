package com.cup.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    Claims claims = null;
    private String username = null;

    // 2) Goes to the if statement and call filterChain.doFilter(request, response) and from there goes to userController
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Inside do Filter {}", request);
        if (request.getServletPath().matches("/user/signUp|/user/login|user/forgotPassword")) {
            log.info("Inside if");
            filterChain.doFilter(request, response);
        }
        else {
            String requestHeader = request.getHeader("Authorization");
            log.info("Request Header: {}", requestHeader);

            String token = null;

            // if the request header is not null and starts with "Bearer"
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                token = requestHeader.substring(7);
                this.username = this.jwtUtils.extractUsername(token);
                this.claims = this.jwtUtils.extractAllClaims(token);
            }

            else log.info("Invalid Header value: {}", requestHeader);

            // if the extracted username is not null and doesn't already have a security context (isn't in a session already)
            if (this.username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.customUserDetailService.loadUserByUsername(this.username);

                if (this.jwtUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) this.claims.get("role"));
    }

    public boolean isUser() {
        return "user".equalsIgnoreCase((String) this.claims.get("role"));
    }

    public String getCurrentUser() {
        return this.username;
    }
}
