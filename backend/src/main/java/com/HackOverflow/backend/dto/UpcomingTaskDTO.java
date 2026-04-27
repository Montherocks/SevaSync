package com.HackOverflow.backend.dto;

public class UpcomingTaskDTO {

    private String name;
    private String description;
    private String date;
    private String location;

    public UpcomingTaskDTO(String name, String description, String date, String location) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}