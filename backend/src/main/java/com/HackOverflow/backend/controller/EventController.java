package com.HackOverflow.backend.controller;


import com.HackOverflow.backend.dto.EventRequestdto;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Create event by admin
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventRequestdto dto,
                                         Principal principal) {

        Event event = eventService.createEvent(dto, principal.getName());
        return ResponseEntity.ok(event);
    }

    // Get all events
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody EventRequestdto dto,
                                         Principal principal) {
        Event updated = eventService.updateEvent(id, dto, principal.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
                                         Principal principal) {
        return ResponseEntity.ok(eventService.deleteEvent(id, principal.getName()));
    }
}