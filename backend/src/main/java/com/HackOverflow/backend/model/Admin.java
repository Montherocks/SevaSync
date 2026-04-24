package com.HackOverflow.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Users {

    private String organization;
    private String location;
    private String intention;

    public Admin() {
    }

    public Admin(String organization, String location, String intention) {
        this.organization = organization;
        this.location = location;
        this.intention = intention;
    }

    public Admin(Long id, String name, String email, String password, RoleType roleType, boolean profileCompleted, String organization, String location, String intention) {
        super(id, name, email, password, roleType, profileCompleted);
        this.organization = organization;
        this.location = location;
        this.intention = intention;
    }

    public Admin(String name, String email, String password, RoleType roleType, boolean profileCompleted, String organization, String location, String intention) {
        super(name, email, password, roleType, profileCompleted);
        this.organization = organization;
        this.location = location;
        this.intention = intention;
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