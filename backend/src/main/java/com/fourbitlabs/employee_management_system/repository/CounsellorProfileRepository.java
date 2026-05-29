package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.CounsellorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounsellorProfileRepository extends JpaRepository<CounsellorProfile, Long> {
    Optional<CounsellorProfile> findByUserId(Long userId);
}
