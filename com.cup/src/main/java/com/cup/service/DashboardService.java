package com.cup.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

public interface DashboardService {

    ResponseEntity<Map<String, Object>> getCount();
}
