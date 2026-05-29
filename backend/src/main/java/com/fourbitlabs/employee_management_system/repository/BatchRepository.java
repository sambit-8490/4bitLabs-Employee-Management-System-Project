package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByTrainerId(Long trainerId);
    List<Batch> findByAnalystId(Long analystId);
}
