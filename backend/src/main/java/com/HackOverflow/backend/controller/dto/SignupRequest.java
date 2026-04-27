package com.HackOverflow.backend.dto;

import com.HackOverflow.backend.model.RoleType;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private RoleType role;
}