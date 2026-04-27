package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/register")
public class EventRegistrationController {

    @Autowired
    private EventRegistrationService registrationService;

    @PostMapping
    public String register(@RequestParam Long eventId, Principal principal) {
        return registrationService.register(eventId, principal.getName());
    }

    //Cancel registration for user
    @DeleteMapping("/cancel")
    public String cancel(@RequestParam Long eventId, Principal principal) {
        return registrationService.cancel(eventId, principal.getName());
    }

    //get regis status
    @GetMapping("/myRegistration")
    public List<Registration> myRegistrations(Principal principal) {
        return registrationService.getMyRegistrations(principal.getName());
    }
}
