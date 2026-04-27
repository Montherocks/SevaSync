package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.AdminRequest;
import com.HackOverflow.backend.dto.LoginRequest;
import com.HackOverflow.backend.dto.SignupRequest;
import com.HackOverflow.backend.dto.VolunteerRequest;
import com.HackOverflow.backend.model.*;
import com.HackOverflow.backend.repository.AdminRepository;
import com.HackOverflow.backend.repository.UserRepository;
import com.HackOverflow.backend.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final VolunteerRepository volunteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, AdminRepository adminRepository, VolunteerRepository volunteerRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.volunteerRepository = volunteerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users(
                req.getName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getRole(),
                false
        );

        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public String login(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        Users user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateToken(user);
    }

    public String registerAdmin(AdminRequest req, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoleType() != RoleType.ADMIN) {
            throw new RuntimeException("Unauthorized role");
        }

        AdminProfile profile = new AdminProfile();
        profile.setUser(user);
        profile.setOrganization(req.getOrganization());
        profile.setLocation(req.getLocation());
        profile.setIntention(req.getIntention());

        adminRepository.save(profile);

        user.setProfileCompleted(true);
        userRepository.save(user);

        return "Admin profile completed";
    }

    public String registerVolunteer(VolunteerRequest req, String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoleType() != RoleType.VOLUNTEER) {
            throw new RuntimeException("Unauthorized role");
        }

        VolunteerProfile profile = new VolunteerProfile();
        profile.setUser(user);
        profile.setSkills(req.getSkills());
        profile.setLocation(req.getLocation());
        profile.setAvailabilityStart(req.getAvailabilityStart());
        profile.setAvailabilityEnd(req.getAvailabilityEnd());

        volunteerRepository.save(profile);

        user.setProfileCompleted(true);
        userRepository.save(user);

        return "Volunteer profile completed";
    }
}