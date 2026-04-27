package com.HackOverflow.backend.service;

import com.HackOverflow.backend.model.*;
import com.HackOverflow.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

    @Autowired
    private RegistrationRepo regRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // Volunteer registers
    public String register(Long eventId, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (regRepo.existsByUserIdAndEventId(user.getId(), eventId)) {
            return "Already Registered!";
        }

        Registration reg = new Registration();
        reg.setUserId(user.getId());
        reg.setEventId(eventId);
        reg.setStatus("PENDING");

        regRepo.save(reg);

        return "Registration Request Sent";
    }

    // Volunteer cancel
    public String cancel(Long eventId, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Registration reg = regRepo.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        regRepo.delete(reg);

        return "Registration Cancelled";
    }

    // Volunteer's registrations
    public List<Registration> getMyRegistrations(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return regRepo.findByUserId(user.getId());
    }

    // Admin pending requests
    public List<Registration> getPendingRegistrations() {
        return regRepo.findByStatus("PENDING");
    }

    // Admin approves
    public String approve(Long registrationId) {

        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setStatus("APPROVED");

        Event event = eventRepository.findById(reg.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Example: each event = 4 hours
        reg.setHoursWorked(4);

        regRepo.save(reg);

        return "Registration Approved";
    }

    // Admin rejects
    public String reject(Long registrationId) {

        Registration reg = regRepo.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        reg.setStatus("REJECTED");

        regRepo.save(reg);

        return "Registration Rejected";
    }

    // TOTAL HOURS
    public int getTotalVolunteerHours(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return regRepo.findByUserIdAndStatus(user.getId(), "APPROVED")
                .stream()
                .filter(r -> {
                    Event e = eventRepository.findById(r.getEventId()).orElse(null);
                    return e != null && e.getDate().isBefore(LocalDate.now());
                })
                .mapToInt(Registration::getHoursWorked)
                .sum();
    }

    // FUTURE TASKS
    public int getPendingTasksCount(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return (int) regRepo.findByUserIdAndStatus(user.getId(), "APPROVED")
                .stream()
                .filter(r -> {
                    Event e = eventRepository.findById(r.getEventId()).orElse(null);
                    return e != null && !e.getDate().isBefore(LocalDate.now());
                })
                .count();
    }

    // COMPLETED TASKS
    public int getCompletedTasksCount(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return (int) regRepo.findByUserIdAndStatus(user.getId(), "APPROVED")
                .stream()
                .filter(r -> {
                    Event e = eventRepository.findById(r.getEventId()).orElse(null);
                    return e != null && e.getDate().isBefore(LocalDate.now());
                })
                .count();
    }
}s