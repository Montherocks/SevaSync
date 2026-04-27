package com.HackOverflow.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VolunteerRequest {
    private String skills;
    private String location;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;
}