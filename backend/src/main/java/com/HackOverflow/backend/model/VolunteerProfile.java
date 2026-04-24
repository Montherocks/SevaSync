package com.HackOverflow.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class VolunteerProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Users user;

    private String skills;
    private String location;

    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;

    public VolunteerProfile() {}

    public VolunteerProfile(Long id, Users user, String skills, String location, LocalDate availabilityStart, LocalDate availabilityEnd) {
        this.id = id;
        this.user = user;
        this.skills = skills;
        this.location = location;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public VolunteerProfile(Users user, String skills, String location, LocalDate availabilityStart, LocalDate availabilityEnd) {
        this.user = user;
        this.skills = skills;
        this.location = location;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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