package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.AnalystProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalystProfileRepository extends JpaRepository<AnalystProfile, Long> {
    Optional<AnalystProfile> findByUserId(Long userId);
}
