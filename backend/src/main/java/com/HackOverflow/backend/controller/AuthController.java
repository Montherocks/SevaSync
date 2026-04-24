package com.HackOverflow.backend.controller;

import com.HackOverflow.backend.dto.*;
import com.HackOverflow.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        String token = authService.signup(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/registeradmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRequest req,
                                           Principal principal) {

        String res = authService.registerAdmin(req, principal.getName());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/registervolunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerRequest req,
                                               Principal principal) {

        String res = authService.registerVolunteer(req, principal.getName());
        return ResponseEntity.ok(res);
    }
}