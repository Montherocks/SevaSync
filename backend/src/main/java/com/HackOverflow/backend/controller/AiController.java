package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.dto.AiEligibilityResponse;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.model.Users;
import com.HackOverflow.backend.model.VolunteerProfile;
import com.HackOverflow.backend.repository.EventRepository;
import com.HackOverflow.backend.repository.UserRepository;
import com.HackOverflow.backend.repository.VolunteerRepository;
import com.HackOverflow.backend.service.AiEligibilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiEligibilityService aiService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;

    public AiController(AiEligibilityService aiService,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        VolunteerRepository volunteerRepository) {

        this.aiService = aiService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.volunteerRepository = volunteerRepository;
    }

    @GetMapping("/check/{eventId}")
    public AiEligibilityResponse checkEligibility(@PathVariable Long eventId,
                                                  Principal principal) {

        Users user = userRepository.findByEmail(principal.getName())
                .orElseThrow();

        VolunteerProfile volunteer = volunteerRepository.findById(user.getId())
                .orElseThrow();

        Event event = eventRepository.findById(eventId)
                .orElseThrow();

        return aiService.analyze(event, volunteer);
    }
}