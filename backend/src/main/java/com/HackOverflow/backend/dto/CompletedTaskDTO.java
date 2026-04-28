package com.HackOverflow.backend.dto;

public class CompletedTaskDTO {

    private String name;
    private int hours;
    private String volunteer;
    private String completedOn;

    public CompletedTaskDTO(String name, int hours, String volunteer, String completedOn) {
        this.name = name;
        this.hours = hours;
        this.volunteer = volunteer;
        this.completedOn = completedOn;
    }

    public String getName() {
        return name;
    }

    public int getHours() {
        return hours;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public String getCompletedOn() {
        return completedOn;
    }
}