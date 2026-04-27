package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findByUserId(Long userId);

    List<Registration> findByStatus(String status);

    List<Registration> findByUserIdAndStatus(Long userId, String status);
}