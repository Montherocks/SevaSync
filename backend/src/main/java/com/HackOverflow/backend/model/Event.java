package com.HackOverflow.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;
    private String location;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String timeZone = "Asia/Kolkata";

    private Double durationHours;

    private String status; // PENDING, ACTIVE, COMPLETED

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "admin_id")
    private AdminProfile admin;
}
