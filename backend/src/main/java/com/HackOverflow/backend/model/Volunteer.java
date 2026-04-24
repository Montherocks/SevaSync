package com.HackOverflow.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Volunteer extends Users {

    private String skills;

    private String location;

    private LocalDate availabilityStart;

    private LocalDate availabilityEnd;

    public Volunteer() {
    }

    public Volunteer(Long id, String name, String email, String password, RoleType roleType, String skills, String location, LocalDate availabilityStart, LocalDate availabilityEnd) {
        super(id, name, email, password, roleType);
        this.skills = skills;
        this.location = location;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public Volunteer(String name, String email, String password, RoleType roleType, String skills, String location, LocalDate availabilityStart, LocalDate availabilityEnd) {
        super(name, email, password, roleType);
        this.skills = skills;
        this.location = location;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(LocalDate availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(LocalDate availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }
}