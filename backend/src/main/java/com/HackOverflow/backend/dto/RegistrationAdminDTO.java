package com.HackOverflow.backend.dto;

import com.HackOverflow.backend.model.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAdminDTO {

    private Long id;
    private Long eventId;
    private String eventName;

    private Long userId;
    private String userName;
    private String email;

    private RegistrationStatus status;
}