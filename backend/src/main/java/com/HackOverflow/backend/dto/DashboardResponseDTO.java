package com.HackOverflow.backend.dto;

import java.util.List;

public class DashboardResponseDTO {

    private double volunteerHours;
    private int tasksPending;
    private int tasksActive;
    private int tasksCompleted;

    private List<UpcomingTaskDTO> upcomingTasks;
    private List<CompletedTaskDTO> completedTasks;

    public DashboardResponseDTO(double volunteerHours,
                                int tasksPending,
                                int tasksActive,
                                int tasksCompleted,
                                List<UpcomingTaskDTO> upcomingTasks,
                                List<CompletedTaskDTO> completedTasks) {
        this.volunteerHours = volunteerHours;
        this.tasksPending = tasksPending;
        this.tasksActive = tasksActive;
        this.tasksCompleted = tasksCompleted;
        this.upcomingTasks = upcomingTasks;
        this.completedTasks = completedTasks;
    }

    public double getVolunteerHours() {
        return volunteerHours;
    }

    public int getTasksPending() {
        return tasksPending;
    }

    public int getTasksActive() {
        return tasksActive;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public List<UpcomingTaskDTO> getUpcomingTasks() {
        return upcomingTasks;
    }

    public List<CompletedTaskDTO> getCompletedTasks() {
        return completedTasks;
    }
}