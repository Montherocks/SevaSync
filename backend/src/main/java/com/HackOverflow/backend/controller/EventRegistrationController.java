package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.service.EventRegistrationService;
import com.HackOverflow.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/register")
public class EventRegistrationController {

    private final EventRegistrationService registrationService;
    private final JwtService jwtService;

<<<<<<< HEAD
    @PostMapping("/{eventId}")
    public String register(@PathVariable Long eventId, Principal principal) {
        return registrationService.register(eventId, principal.getName());
=======
    @Autowired
    public EventRegistrationController(EventRegistrationService registrationService,
                                       JwtService jwtService) {
        this.registrationService = registrationService;
        this.jwtService = jwtService;
>>>>>>> 936fd9f78bf9441ed59c1c19b9e18bab88c9274a
    }

    @PostMapping
    public String register(@RequestParam Long eventId,
                           HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Unauthorized";
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        return registrationService.register(eventId, email);
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