package com.HackOverflow.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private boolean profileCompleted = false;

    public Users() {
    }

    public Users(Long id, String name, String email, String password, RoleType roleType, boolean profileCompleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileCompleted = profileCompleted;
    }

    public Users(String name, String email, String password, RoleType roleType, boolean profileCompleted) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileCompleted = profileCompleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
}