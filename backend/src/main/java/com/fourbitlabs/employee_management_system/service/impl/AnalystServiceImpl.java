package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.service.interfaces.*;

import com.fourbitlabs.employee_management_system.dto.request.AnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateAnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AnalystResponseDto;
import com.fourbitlabs.employee_management_system.entity.AnalystProfile;
import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;
import com.fourbitlabs.employee_management_system.exception.DuplicateResourceException;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.AnalystProfileRepository;
import com.fourbitlabs.employee_management_system.repository.BatchRepository;
import com.fourbitlabs.employee_management_system.repository.RefreshTokenRepository;
import com.fourbitlabs.employee_management_system.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalystServiceImpl implements AnalystService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalystProfileRepository analystProfileRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AnalystResponseDto createAnalyst(AnalystRequestDto analystRequestDto) {
        // Check for duplicate email
        if (userRepository.existsByEmail(analystRequestDto.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + analystRequestDto.getEmail() + "' already exists");
        }
        User admin = userRepository.findById(analystRequestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "A admin with this id: " + analystRequestDto.getAdminId() + " is not found."));

        User user = new User();
        user.setName(analystRequestDto.getName());
        user.setEmail(analystRequestDto.getEmail());
        user.setPhone(analystRequestDto.getPhone());
        user.setPassword(passwordEncoder.encode(analystRequestDto.getPassword()));
        user.setRole(Role.ANALYST);
        user.setStatus(analystRequestDto.getUserStatus() != null ? analystRequestDto.getUserStatus() : UserStatus.ACTIVE);
        user.setCreatedByAdmin(admin);
        User savedUser = userRepository.save(user);
        user.getManagedUsers().add(savedUser);

        AnalystProfile analystProfile = new AnalystProfile();
        analystProfile.setDepartment(analystRequestDto.getDepartment());
        analystProfile.setJoiningDate(analystRequestDto.getJoiningDate());
        analystProfile.setSalary(analystRequestDto.getSalary());
        analystProfile.setUser(savedUser);
        AnalystProfile savedAnalystProfile = analystProfileRepository.save(analystProfile);

        return mapToResponseDto(savedUser, savedAnalystProfile);
    }

    @Override
    public List<AnalystResponseDto> getAllAnalysts() {
        List<User> analysts = userRepository.findByRole(Role.ANALYST);
        return analysts.stream()
                .map(user -> {
                    AnalystProfile profile = analystProfileRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Analyst profile not found for user id: " + user.getId()));
                    return mapToResponseDto(user, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public AnalystResponseDto getAnalystById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found with id: " + userId));

        if (user.getRole() != Role.ANALYST) {
            throw new ResourceNotFoundException("User with id " + userId + " is not an analyst");
        }

        AnalystProfile profile = analystProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst profile not found for user id: " + userId));

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public AnalystResponseDto updateAnalyst(Long userId, UpdateAnalystRequestDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found with id: " + userId));

        if (user.getRole() != Role.ANALYST) {
            throw new ResourceNotFoundException("User with id " + userId + " is not an analyst");
        }

        AnalystProfile profile = analystProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst profile not found for user id: " + userId));

        // Update User fields (only non-null values)
        if (updateDto.getName() != null) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        userRepository.save(user);

        // Update AnalystProfile fields (only non-null values)
        if (updateDto.getDepartment() != null) {
            profile.setDepartment(updateDto.getDepartment());
        }
        if (updateDto.getJoiningDate() != null) {
            profile.setJoiningDate(updateDto.getJoiningDate());
        }
        if (updateDto.getSalary() != null) {
            profile.setSalary(updateDto.getSalary());
        }
        if (updateDto.getUserStatus() != null) {
            user.setStatus(updateDto.getUserStatus());
        }
        analystProfileRepository.save(profile);

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public void deleteAnalyst(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found with id: " + userId));

        if (user.getRole() != Role.ANALYST) {
            throw new ResourceNotFoundException("User with id " + userId + " is not an analyst");
        }

        analystProfileRepository.findByUserId(userId)
                .ifPresent(profile -> {
                    List<com.fourbitlabs.employee_management_system.entity.Batch> batches = batchRepository.findByAnalystId(profile.getId());
                    if (batches != null) {
                        for (com.fourbitlabs.employee_management_system.entity.Batch b : batches) {
                            b.setAnalyst(null);
                            batchRepository.save(b);
                        }
                    }

                    analystProfileRepository.delete(profile);
                });
        
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @NotNull
    private static AnalystResponseDto mapToResponseDto(User savedUser, AnalystProfile savedAnalystProfile) {
        AnalystResponseDto analystResponseDto = new AnalystResponseDto();
        analystResponseDto.setId(savedUser.getId());
        analystResponseDto.setName(savedUser.getName());
        analystResponseDto.setEmail(savedUser.getEmail());
        analystResponseDto.setPhone(savedUser.getPhone());
        analystResponseDto.setRole(savedUser.getRole());
        analystResponseDto.setStatus(savedUser.getStatus());

        analystResponseDto.setDepartment(savedAnalystProfile.getDepartment());
        analystResponseDto.setJoiningDate(savedAnalystProfile.getJoiningDate());
        analystResponseDto.setSalary(savedAnalystProfile.getSalary());
        return analystResponseDto;
    }
}
