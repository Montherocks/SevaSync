package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.model.Users;
import com.HackOverflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class AdminProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public Users getAdminProfile(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }
}