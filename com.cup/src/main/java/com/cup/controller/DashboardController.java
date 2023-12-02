package com.cup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/dashboard")
public interface DashboardController {

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getCount();
}
