package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminProfile, Long> {
}