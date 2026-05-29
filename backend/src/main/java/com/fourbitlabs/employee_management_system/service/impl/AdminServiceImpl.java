package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.service.interfaces.*;

import com.fourbitlabs.employee_management_system.dto.request.AdminRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AdminResponseDto;
import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;
import com.fourbitlabs.employee_management_system.exception.DuplicateResourceException;
import com.fourbitlabs.employee_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDto createAdmin(AdminRequestDto createAdminRequestDto) {
        // Check for duplicate email
        if (userRepository.existsByEmail(createAdminRequestDto.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + createAdminRequestDto.getEmail() + "' already exists");
        }
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new DuplicateResourceException("An admin already exists");
        }

        User user = new User();
        user.setName(createAdminRequestDto.getName());
        user.setEmail(createAdminRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(createAdminRequestDto.getPassword()));
        user.setPhone(createAdminRequestDto.getPhone());
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedByAdmin(null);
        User savedUser = userRepository.save(user);
        user.getManagedUsers().add(savedUser);

        AdminResponseDto responseDto = new AdminResponseDto();
        responseDto.setId(savedUser.getId());
        responseDto.setName(savedUser.getName());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setPhone(savedUser.getPhone());
        responseDto.setRole(savedUser.getRole());
        responseDto.setStatus(savedUser.getStatus());

        return responseDto;
    }
}
