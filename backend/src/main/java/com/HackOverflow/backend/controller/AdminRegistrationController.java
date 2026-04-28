package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.dto.RegistrationAdminDTO;
import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/registrations")
public class AdminRegistrationController {

    @Autowired
    private EventRegistrationService service;

    @GetMapping("/all")
    public List<RegistrationAdminDTO> getAll() {
        return service.getAllRegistrations();
    }

    @PutMapping("/approve")
    public String approve(@RequestParam Long id) {
        return service.approve(id);
    }

    @PutMapping("/reject")
    public String reject(@RequestParam Long id) {
        return service.reject(id);
    }

    @GetMapping("/active")
    public List<RegistrationAdminDTO> getActiveTasks() {
        return service.getActiveTasks();
    }

    @GetMapping("/pending")
    public List<RegistrationAdminDTO> getPendingTasks() {
        return service.getPendingTasks();
    }

    @GetMapping("/completed")
    public List<RegistrationAdminDTO> getCompletedTasks() {
        return service.getCompletedTasks();
    }
}

