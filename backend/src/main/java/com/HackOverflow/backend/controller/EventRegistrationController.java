package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class EventRegistrationController {

    @Autowired
    private EventRegistrationService registrationService;

    @PostMapping
    public String register(@RequestParam Long userId,
                           @RequestParam Long eventId) {
        return registrationService.register(userId, eventId);
    }
}
