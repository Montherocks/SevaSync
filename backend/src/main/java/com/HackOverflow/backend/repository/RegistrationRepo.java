package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.AdminProfile;
import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    List<Registration> findAll();
    Optional<Registration> findByUserIdAndEventId(Long id, Long eventId);
    List<Registration> findByUserIdAndStatus(Long userId, RegistrationStatus status);
    List<Registration> findByUserId(Long id);
}