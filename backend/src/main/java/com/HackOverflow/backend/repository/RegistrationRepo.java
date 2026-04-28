package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.Registration;
import com.HackOverflow.backend.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findByUserIdAndStatus(Long userId, RegistrationStatus status);

    List<Registration> findByUserId(Long userId);

    List<Registration> findByStatus(RegistrationStatus status);

    @Query("""
        SELECT r
        FROM Registration r
        JOIN Event e ON r.eventId = e.id
        WHERE r.status = 'APPROVED'
        AND e.date >= CURRENT_DATE
    """)
    List<Registration> findActiveTasks();

    @Query("""
        SELECT r
        FROM Registration r
        JOIN Event e ON r.eventId = e.id
        WHERE r.status = 'APPROVED'
        AND e.date < CURRENT_DATE
    """)
    List<Registration> findCompletedTasks();

}
