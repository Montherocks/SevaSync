package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
public class AdminTaskController {

    @Autowired
    private EventRepository eventRepository;

    // use ONLY time because Event uses LocalTime
    private LocalTime now() {
        return LocalTime.now();
    }

    // ACTIVE COUNT
    @GetMapping("/active/count")
    public long getActiveCount() {
        LocalTime now = now();
        return eventRepository.countByStartTimeBeforeAndEndTimeAfter(now, now);
    }

    // PENDING COUNT
    @GetMapping("/pending/count")
    public long getPendingCount() {
        LocalTime now = now();
        return eventRepository.countByStartTimeAfter(now);
    }

    // COMPLETED COUNT
    @GetMapping("/completed/count")
    public long getCompletedCount() {
        LocalTime now = now();
        return eventRepository.countByEndTimeBefore(now);
    }

    // COMPLETED LIST
    @GetMapping("/completed")
    public List<Event> getCompletedEvents() {
        LocalTime now = now();
        return eventRepository.findByEndTimeBefore(now);
    }
}