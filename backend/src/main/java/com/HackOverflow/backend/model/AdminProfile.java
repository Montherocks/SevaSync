package com.HackOverflow.backend.model;

import jakarta.persistence.*;

@Entity
public class AdminProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Users user;

    private String organization;
    private String location;
    private String intention;

    public AdminProfile() {}

    public AdminProfile(Long id, Users user, String organization, String location, String intention) {
        this.id = id;
        this.user = user;
        this.organization = organization;
        this.location = location;
        this.intention = intention;
    }

    public AdminProfile(Users user, String organization, String location, String intention) {
        this.user = user;
        this.organization = organization;
        this.location = location;
        this.intention = intention;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }
}