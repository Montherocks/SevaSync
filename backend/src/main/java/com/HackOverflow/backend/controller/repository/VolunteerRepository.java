package com.HackOverflow.backend.repository;


import com.HackOverflow.backend.model.VolunteerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerProfile, Long> {
}