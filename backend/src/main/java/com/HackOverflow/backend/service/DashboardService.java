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

@Service
public class DashboardService {

    @Autowired
    private RegistrationRepo registrationRepo;

    @Autowired
    private EventRepository eventRepository;

    public DashboardResponseDTO getVolunteerDashboard(Long userId) {

        LocalDate today = LocalDate.now();

        // ONLY approved registrations
        List<Registration> registrations =
                registrationRepo.findByUserIdAndStatus(userId, RegistrationStatus.APPROVED);

        int totalHours = 0;
        int pendingTasks = 0;
        int completedTasks = 0;

        List<UpcomingTaskDTO> upcomingTasks = new ArrayList<>();
        List<CompletedTaskDTO> completedTaskList = new ArrayList<>();

        for (Registration reg : registrations) {

            // fetch event using eventId (since your model stores IDs, not objects)
            Event event = eventRepository.findById(reg.getEventId())
                    .orElse(null);

            if (event == null) continue;

            LocalDate eventDate = event.getDate();

            // UPCOMING TASKS
            if (eventDate.isAfter(today)) {

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
            // COMPLETED TASKS
            else {

                completedTasks++;
                totalHours += 1; // or event.getHours() if you later add it

                completedTaskList.add(
                        new CompletedTaskDTO(
                                event.getName(),
                                1,
                                "Volunteer",
                                event.getDate().toString()
                        )
                );
            }
        }

        return new DashboardResponseDTO(
                totalHours,
                pendingTasks,
                completedTasks,
                upcomingTasks,
                completedTaskList
        );
    }
}