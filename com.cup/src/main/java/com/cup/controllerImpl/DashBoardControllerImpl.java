package com.cup.controllerImpl;

import com.cup.controller.DashboardController;
import com.cup.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashBoardControllerImpl implements DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return this.dashboardService.getCount();
    }
}
