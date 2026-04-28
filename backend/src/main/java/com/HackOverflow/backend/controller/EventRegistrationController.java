package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.service.EventRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/register")
public class EventRegistrationController {
    private static final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);
    @Autowired
    private EventRegistrationService registrationService;

    @PostMapping("/{eventId}")
    public String register(@PathVariable Long eventId, Principal principal) {
        log.info("User {} is trying to register for event {}", principal.getName(), eventId);
        return registrationService.register(eventId, principal.getName());
    }

    // Cancel registration for user
    @DeleteMapping("/cancel")
    public String cancel(@RequestParam Long eventId, Principal principal) {
        return registrationService.cancel(eventId, principal.getName());
    }

    // Get registration status
    @GetMapping("/myRegistration")
    public List<Registration> myRegistrations(Principal principal) {
        return registrationService.getMyRegistrations(principal.getName());
    }
}