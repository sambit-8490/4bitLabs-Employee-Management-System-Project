package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.BatchProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchProgressRepository extends JpaRepository<BatchProgress, Long> {
    List<BatchProgress> findByBatchId(Long batchId);
    List<BatchProgress> findByTrainerId(Long trainerId);
}
