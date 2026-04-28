package com.HackOverflow.backend.dto;

public class CompletedTaskDTO {

    private String name;
    private double hours;
    private String volunteer;
    private String completedOn;

    public CompletedTaskDTO(String name, double hours, String volunteer, String completedOn) {
        this.name = name;
        this.hours = hours;
        this.volunteer = volunteer;
        this.completedOn = completedOn;
    }

    public String getName() {
        return name;
    }

    public double getHours() {
        return hours;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public String getCompletedOn() {
        return completedOn;
    }
}