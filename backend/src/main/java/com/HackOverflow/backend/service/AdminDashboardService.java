package com.HackOverflow.backend.service;

import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {

    @Autowired
    private EventRepository eventRepository;

    public Map<String, Object> getDashboardData() {

        LocalDate today = LocalDate.now();

        List<Event> allEvents = eventRepository.findAll();

        List<Event> active = allEvents.stream()
                .filter(event -> event.getDate().isEqual(today))
                .collect(Collectors.toList());

        List<Event> pending = allEvents.stream()
                .filter(event -> event.getDate().isAfter(today))
                .collect(Collectors.toList());

        List<Event> completed = allEvents.stream()
                .filter(event -> event.getDate().isBefore(today))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();

        response.put("activeCount", active.size());
        response.put("pendingCount", pending.size());
        response.put("completedCount", completed.size());

        response.put("activeTasks", active);
        response.put("pendingTasks", pending);
        response.put("completedTasks", completed);

        return response;
    }
}