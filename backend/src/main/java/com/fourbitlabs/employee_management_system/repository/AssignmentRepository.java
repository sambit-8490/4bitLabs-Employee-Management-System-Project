package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.Assignment;
import com.fourbitlabs.employee_management_system.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByStudentIdAndStatus(Long studentId, AssignmentStatus status);
    List<Assignment> findByBatchIdAndStatus(Long batchId, AssignmentStatus status);
    List<Assignment> findByStudentId(Long studentId);
    List<Assignment> findByBatchId(Long batchId);
}
