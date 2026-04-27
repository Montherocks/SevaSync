package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.CompletedTaskDTO;
import com.HackOverflow.backend.dto.DashboardResponseDTO;
import com.HackOverflow.backend.dto.UpcomingTaskDTO;
import com.HackOverflow.backend.model.EventRegistration;
import com.HackOverflow.backend.repository.EventRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private EventRegistrationRepository registrationRepository;

    public DashboardResponseDTO getVolunteerDashboard(Long userId) {

        List<EventRegistration> registrations =
                registrationRepository.findByUserIdAndStatus(userId, "APPROVED");

        int totalHours = 0;
        int pendingTasks = 0;
        int completedTasks = 0;

        List<UpcomingTaskDTO> upcomingTasks = new ArrayList<>();
        List<CompletedTaskDTO> completedTaskList = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (EventRegistration reg : registrations) {

            LocalDate eventDate = reg.getEvent().getDate();

            if (eventDate.isAfter(today)) {
                pendingTasks++;

                upcomingTasks.add(
                        new UpcomingTaskDTO(
                                reg.getEvent().getName(),
                                reg.getEvent().getDescription(),
                                reg.getEvent().getDate().toString(),
                                reg.getEvent().getLocation()
                        )
                );

            } else if (eventDate.isBefore(today) || eventDate.isEqual(today)) {

                completedTasks++;
                totalHours += reg.getEvent().getHours();

                completedTaskList.add(
                        new CompletedTaskDTO(
                                reg.getEvent().getName(),
                                reg.getEvent().getHours(),
                                reg.getUser().getName(),
                                reg.getEvent().getDate().toString()
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