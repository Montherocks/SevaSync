package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.CompletedTaskDTO;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardService {

    @Autowired
    private EventRepository eventRepository;

    public Map<String, Object> getDashboardData() {

        LocalDate today = LocalDate.now();

        List<Event> allEvents = eventRepository.findAll();

        // Active = today
        List<Event> active = allEvents.stream()
                .filter(event -> event.getDate().isEqual(today))
                .toList();

        // Pending = future
        List<Event> pending = allEvents.stream()
                .filter(event -> event.getDate().isAfter(today))
                .toList();

        // Completed = past
        List<Event> completedEvents = allEvents.stream()
                .filter(event -> event.getDate().isBefore(today))
                .toList();

        // Convert ONLY completed events → DTO (NO entity leakage)
        List<CompletedTaskDTO> completed = completedEvents.stream()
        .map(e -> new CompletedTaskDTO(
                e.getName(),
                e.getDurationHours() != null ? e.getDurationHours().intValue() : 0,
                "Admin",
                e.getDate().toString()
        ))
        .toList();

        Map<String, Object> response = new HashMap<>();

        response.put("activeCount", active.size());
        response.put("pendingCount", pending.size());
        response.put("completedCount", completed.size());

        response.put("activeTasks", active);
        response.put("pendingTasks", pending);

        // IMPORTANT: frontend gets DTOs only
        response.put("completedTasks", completed);

        return response;
    }
}