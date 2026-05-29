package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.Student;
import com.fourbitlabs.employee_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCounsellorId(Long counsellorId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
