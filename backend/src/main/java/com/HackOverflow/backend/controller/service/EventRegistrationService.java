package com.HackOverflow.backend.service;


import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.repository.RegistrationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventRegistrationService {

    @Autowired
    private RegistrationRepo regRepo;
    public String register(Long userId, Long eventId) {
        if (regRepo.existsByUserIdAndEventId(userId, eventId)) {
            return "Already Registered!";
        }

        Registration reg = new Registration();
        reg.setUserId(userId);
        reg.setEventId(eventId);

        regRepo.save(reg);

        return "Registration Successful";
    }
}
