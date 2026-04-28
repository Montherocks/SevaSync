package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // ACTIVE: started but not ended
    long countByStartTimeBeforeAndEndTimeAfter(LocalTime now1, LocalTime now2);

    // PENDING: not started yet
    long countByStartTimeAfter(LocalTime now);

    // COMPLETED: already ended
    long countByEndTimeBefore(LocalTime now);

    // LIST of completed tasks
    List<Event> findByEndTimeBefore(LocalTime now);
}