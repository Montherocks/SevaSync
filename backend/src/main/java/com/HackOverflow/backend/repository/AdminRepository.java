package com.HackOverflow.backend.repository;

import com.HackOverflow.backend.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<AdminProfile, Long> {
      Optional<AdminProfile> findByUser(Users user);
}
