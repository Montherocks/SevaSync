package com.HackOverflow.backend.service;


import com.HackOverflow.backend.dto.EventRequestdto;
import com.HackOverflow.backend.model.AdminProfile;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.model.RoleType;
import com.HackOverflow.backend.model.Users;
import com.HackOverflow.backend.repository.AdminRepository;
import com.HackOverflow.backend.repository.EventRepository;
import com.HackOverflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(EventRequestdto dto, String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoleType() != RoleType.ADMIN) {
            throw new RuntimeException("Only ADMIN can create events");
        }
        AdminProfile admin = adminRepository.findByUser(user)
                .orElseThrow(()->new RuntimeException("Admin Profile Not Found"));

        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setCategory(dto.getCategory());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        event.setTime(dto.getTime());

        event.setAdmin(admin);

        return eventRepository.save(event);
    }
}