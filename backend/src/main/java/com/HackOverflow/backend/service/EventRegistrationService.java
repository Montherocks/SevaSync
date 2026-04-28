package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.RegistrationAdminDTO;
import com.HackOverflow.backend.model.*;
import com.HackOverflow.backend.repository.EventRepository;
import com.HackOverflow.backend.repository.RegistrationRepo;
import com.HackOverflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    @Autowired
    private RegistrationRepo regRepo;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // REGISTER USER
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

    // CANCEL
    public String cancel(Long eventId, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Registration reg = regRepo.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Not registered"));

        regRepo.delete(reg);

        return "Registration Cancelled";
    }

    // MY REGISTRATIONS
    public List<RegistrationAdminDTO> getMyRegistrations(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Registration> regs = regRepo.findByUserId(user.getId());

        return regs.stream().map(reg -> {

            Event event = eventRepository.findById(reg.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            return new RegistrationAdminDTO(
                    reg.getId(),
                    event.getId(),
                    event.getName(),
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    reg.getStatus()
            );

        }).toList();
    }

    // ADMIN: ALL REGISTRATIONS
    public List<RegistrationAdminDTO> getAllRegistrations() {

        List<Registration> regs = regRepo.findAll();

        return regs.stream().map(reg -> {

            Users user = userRepository.findById(reg.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Event event = eventRepository.findById(reg.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            return new RegistrationAdminDTO(
                    reg.getId(),
                    event.getId(),
                    event.getName(),
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    reg.getStatus()
            );

        }).toList();
    }

    // APPROVE
    public String approve(Long id) {

        Registration reg = regRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setStatus(RegistrationStatus.APPROVED);
        regRepo.save(reg);

        return "Registration Approved";
    }

    // REJECT
    public String reject(Long id) {

        Registration reg = regRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setStatus(RegistrationStatus.REJECTED);
        regRepo.save(reg);

        return "Registration Rejected";
    }
}
