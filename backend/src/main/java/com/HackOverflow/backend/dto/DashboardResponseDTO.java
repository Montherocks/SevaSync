package com.HackOverflow.backend.dto;

import java.util.List;

public class DashboardResponseDTO {

    private int volunteerHours;
    private int tasksPending;
    private int tasksCompleted;

    private List<UpcomingTaskDTO> upcomingTasks;
    private List<CompletedTaskDTO> completedTasks;

    public DashboardResponseDTO(int volunteerHours, int tasksPending, int tasksCompleted,
                                List<UpcomingTaskDTO> upcomingTasks,
                                List<CompletedTaskDTO> completedTasks) {
        this.volunteerHours = volunteerHours;
        this.tasksPending = tasksPending;
        this.tasksCompleted = tasksCompleted;
        this.upcomingTasks = upcomingTasks;
        this.completedTasks = completedTasks;
    }

    public int getVolunteerHours() {
        return volunteerHours;
    }

    public int getTasksPending() {
        return tasksPending;
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