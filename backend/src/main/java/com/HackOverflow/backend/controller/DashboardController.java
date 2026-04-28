package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.dto.DashboardResponseDTO;
import com.HackOverflow.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponseDTO> getDashboard(@PathVariable Long userId) {
        return ResponseEntity.ok(
                dashboardService.getVolunteerDashboard(userId)
        );
    }
}