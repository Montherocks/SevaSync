package com.HackOverflow.backend.dto;

import lombok.Data;

@Data
public class AdminRequest {
    private String organization;
    private String location;
    private String intention;
}