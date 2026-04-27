package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.HackOverflow.backend.model.Registration;

import java.util.Map;
import java.util.List;

@RestController
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping("/admin/dashboard")
    public Map<String, Object> getDashboard() {
        return adminDashboardService.getDashboardData();
    }
    
}
