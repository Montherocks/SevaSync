package com.HackOverflow.backend.service;


import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.model.RegistrationStatus;
import com.HackOverflow.backend.model.Users;
import com.HackOverflow.backend.repository.RegistrationRepo;
import com.HackOverflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class EventRegistrationService {

    @Autowired
    private RegistrationRepo regRepo;

    @Autowired
    private UserRepository userRepository;
    // Voln service
    public String register(Long eventId, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();
        if (regRepo.existsByUserIdAndEventId(userId, eventId)) {
            return "Already Registered!";
        }

        Registration reg = new Registration();
        reg.setUserId(userId);
        reg.setEventId(eventId);
        reg.setStatus(RegistrationStatus.PENDING);
        regRepo.save(reg);

        return "Request Sent (Wait For Approval)";
    }

    public String cancel(Long eventId, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Registration reg = regRepo.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Not registered"));
        regRepo.delete(reg);
        return "Registration Cancelled";
    }

    //get his/her regis
    public List<Registration> getMyRegistrations(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return regRepo.findByUserId(user.getId());
    }





    // Admin Service  --> fetch all reg,app,reject
    public List<Registration> getAllRegistrations() {
        return regRepo.findAll();
    }

    public String approve(Long id) {
        Registration reg=regRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Registration not found"));
        reg.setStatus(RegistrationStatus.APPROVED);
        return "Registration Approved";
    }


    public String reject(Long id) {
        Registration reg=regRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Registration not found"));
        reg.setStatus(RegistrationStatus.REJECTED);
        return "Registration Rejected";
    }

}