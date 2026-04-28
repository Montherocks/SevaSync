package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.CompletedTaskDTO;
import com.HackOverflow.backend.dto.DashboardResponseDTO;
import com.HackOverflow.backend.dto.UpcomingTaskDTO;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.model.RegistrationStatus;
import com.HackOverflow.backend.repository.EventRepository;
import com.HackOverflow.backend.repository.RegistrationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

@Service
public class DashboardService {

    @Autowired
    private RegistrationRepo registrationRepo;

    @Autowired
    private EventRepository eventRepository;

	public DashboardResponseDTO getVolunteerDashboard(Long userId) {

    List<Registration> registrations =
            registrationRepo.findByUserIdAndStatus(userId, RegistrationStatus.APPROVED);

    double totalHours = 0;
    int pendingTasks = 0;
    int activeTasks = 0;
    int completedTasks = 0;

    List<UpcomingTaskDTO> upcomingTasks = new ArrayList<>();
    List<CompletedTaskDTO> completedTaskList = new ArrayList<>();

    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    for (Registration reg : registrations) {

        Event event = eventRepository.findById(reg.getEventId())
                .orElse(null);

        if (event == null) continue;

        LocalDate eventDate = event.getDate();
        LocalTime start = event.getStartTime();
        LocalTime end = event.getEndTime();

        double duration = event.getDurationHours() != null ? event.getDurationHours() : 1;

        // =========================
        // 🔵 ACTIVE TASKS
        // =========================
        if (eventDate.isEqual(today)
                && start.isBefore(now)
                && end.isAfter(now)) {

            activeTasks++;

        }

        // =========================
        // 🟡 PENDING TASKS
        // =========================
        else if (eventDate.isAfter(today)
                || (eventDate.isEqual(today) && start.isAfter(now))) {

            pendingTasks++;

            upcomingTasks.add(
                    new UpcomingTaskDTO(
                            event.getName(),
                            event.getDescription(),
                            event.getDate().toString(),
                            event.getLocation()
                    )
            );
        }

        // =========================
        // 🔴 COMPLETED TASKS
        // =========================
        else {

            completedTasks++;
            totalHours += duration;

            completedTaskList.add(
                    new CompletedTaskDTO(
                            event.getName(),
                            duration,
                            "Volunteer",
                            event.getDate().toString()
                    )
            );
        }
    }

    return new DashboardResponseDTO(
        totalHours,
        pendingTasks,
        activeTasks,
        completedTasks,
        upcomingTasks,
        completedTaskList
    );
    }
}