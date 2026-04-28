package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.dto.*;
import com.HackOverflow.backend.model.Users;
import com.HackOverflow.backend.repository.UserRepository;
import com.HackOverflow.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {

        String token = authService.signup(req);

        Users user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                Map.of(
                        "jwtToken", token,
                        "role", user.getRoleType().name()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        String token = authService.login(req);

        Users user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                Map.of(
                        "jwtToken", token,
                        "role", user.getRoleType().name()
                )
        );
    }

    @PostMapping("/registeradmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRequest req,
                                           Principal principal) {

        String res = authService.registerAdmin(req, principal.getName());
        return ResponseEntity.ok(Map.of("success", true, "message", res));
    }

    @PostMapping("/registervolunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerRequest req,
                                               Principal principal) {

        String res = authService.registerVolunteer(req, principal.getName());
        return ResponseEntity.ok(Map.of("success", true, "message", res));
    }
}