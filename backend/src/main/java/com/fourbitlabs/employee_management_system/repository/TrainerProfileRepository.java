package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.TrainerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerProfileRepository extends JpaRepository<TrainerProfile, Long> {
    Optional<TrainerProfile> findByUserId(Long userId);
}
