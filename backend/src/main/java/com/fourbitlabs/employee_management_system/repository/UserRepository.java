package com.fourbitlabs.employee_management_system.repository;

import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndRole(String email, Role role);
    List<User> findByRole(Role role);
    boolean existsByEmail(String email);
    boolean existsByRole(Role role);
}
