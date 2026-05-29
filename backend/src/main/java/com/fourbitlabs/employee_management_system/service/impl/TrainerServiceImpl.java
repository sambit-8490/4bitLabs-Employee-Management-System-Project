package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.service.interfaces.*;

import com.fourbitlabs.employee_management_system.dto.request.TrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateTrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.TrainerResponseDto;
import com.fourbitlabs.employee_management_system.entity.TrainerProfile;
import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;
import com.fourbitlabs.employee_management_system.dto.request.BatchProgressRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.BatchProgressResponseDto;
import com.fourbitlabs.employee_management_system.entity.BatchProgress;
import com.fourbitlabs.employee_management_system.entity.Batch;
import com.fourbitlabs.employee_management_system.repository.BatchProgressRepository;
import com.fourbitlabs.employee_management_system.repository.BatchRepository;
import com.fourbitlabs.employee_management_system.repository.RefreshTokenRepository;
import com.fourbitlabs.employee_management_system.exception.DuplicateResourceException;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.TrainerProfileRepository;
import com.fourbitlabs.employee_management_system.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerProfileRepository trainerProfileRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private BatchProgressRepository batchProgressRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TrainerResponseDto createTrainer(TrainerRequestDto trainerRequestDto) {
        // Check for duplicate email
        if (userRepository.existsByEmail(trainerRequestDto.getEmail())) {
            throw new DuplicateResourceException("A user with email '" + trainerRequestDto.getEmail() + "' already exists");
        }
        User admin = userRepository.findById(trainerRequestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("A admin with this id: "+ trainerRequestDto.getAdminId() + " is not found."));

        User user = new User();
        user.setName(trainerRequestDto.getName());
        user.setEmail(trainerRequestDto.getEmail());
        user.setPhone(trainerRequestDto.getPhone());
        user.setPassword(passwordEncoder.encode(trainerRequestDto.getPassword()));
        user.setRole(Role.TRAINER);
        user.setStatus(trainerRequestDto.getUserStatus() != null ? trainerRequestDto.getUserStatus() : UserStatus.ACTIVE);
        user.setCreatedByAdmin(admin);
        User savedUser = userRepository.save(user);
        user.getManagedUsers().add(savedUser);

        TrainerProfile trainerProfile = new TrainerProfile();
        trainerProfile.setSpecialization(trainerRequestDto.getSpecialization());
        trainerProfile.setExperienceYears(trainerRequestDto.getExperienceYears());
        trainerProfile.setQualification(trainerRequestDto.getQualification());
        trainerProfile.setSalary(trainerRequestDto.getSalary());
        trainerProfile.setJoiningDate(trainerRequestDto.getJoiningDate());
        trainerProfile.setUser(savedUser);
        TrainerProfile savedTrainerProfile = trainerProfileRepository.save(trainerProfile);

        return mapToResponseDto(savedUser, savedTrainerProfile);
    }

    @Override
    public List<TrainerResponseDto> getAllTrainers() {
        List<User> trainers = userRepository.findByRole(Role.TRAINER);
        return trainers.stream()
                .map(user -> {
                    TrainerProfile profile = trainerProfileRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Trainer profile not found for user id: " + user.getId()));
                    return mapToResponseDto(user, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public TrainerResponseDto getTrainerById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + userId));

        if (user.getRole() != Role.TRAINER) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a trainer");
        }

        TrainerProfile profile = trainerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer profile not found for user id: " + userId));

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public TrainerResponseDto updateTrainer(Long userId, UpdateTrainerRequestDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + userId));

        if (user.getRole() != Role.TRAINER) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a trainer");
        }

        TrainerProfile profile = trainerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer profile not found for user id: " + userId));

        // Update User fields (only non-null values)
        if (updateDto.getName() != null) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        userRepository.save(user);

        // Update TrainerProfile fields (only non-null values)
        if (updateDto.getSpecialization() != null) {
            profile.setSpecialization(updateDto.getSpecialization());
        }
        if (updateDto.getExperienceYears() != null) {
            profile.setExperienceYears(updateDto.getExperienceYears());
        }
        if (updateDto.getQualification() != null) {
            profile.setQualification(updateDto.getQualification());
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

        trainerProfileRepository.save(profile);

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public void deleteTrainer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + userId));

        if (user.getRole() != Role.TRAINER) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a trainer");
        }

        trainerProfileRepository.findByUserId(userId)
                .ifPresent(profile -> {
                    List<Batch> batches = batchRepository.findByTrainerId(profile.getId());
                    if (batches != null) {
                        for (Batch b : batches) {
                            b.setTrainer(null);
                            batchRepository.save(b);
                        }
                    }

                    List<BatchProgress> progresses = batchProgressRepository.findByTrainerId(profile.getId());
                    if (progresses != null) {
                        for (BatchProgress p : progresses) {
                            p.setTrainer(null);
                            batchProgressRepository.save(p);
                        }
                    }

                    trainerProfileRepository.delete(profile);
                });
        
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @NotNull
    private static TrainerResponseDto mapToResponseDto(User savedUser, TrainerProfile savedTrainerProfile) {
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto();
        trainerResponseDto.setId(savedUser.getId());
        trainerResponseDto.setName(savedUser.getName());
        trainerResponseDto.setEmail(savedUser.getEmail());
        trainerResponseDto.setPhone(savedUser.getPhone());
        trainerResponseDto.setRole(savedUser.getRole());
        trainerResponseDto.setStatus(savedUser.getStatus());

        trainerResponseDto.setSpecialization(savedTrainerProfile.getSpecialization());
        trainerResponseDto.setQualification(savedTrainerProfile.getQualification());
        trainerResponseDto.setExperienceYears(savedTrainerProfile.getExperienceYears());
        trainerResponseDto.setJoiningDate(savedTrainerProfile.getJoiningDate());
        trainerResponseDto.setSalary(savedTrainerProfile.getSalary());
        return trainerResponseDto;
    }

    @Override
    public BatchProgressResponseDto addBatchProgress(org.springframework.web.multipart.MultipartFile file, BatchProgressRequestDto batchProgressRequestDto) {
        Batch batch = batchRepository.findById(batchProgressRequestDto.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchProgressRequestDto.getBatchId()));

        TrainerProfile trainerProfile = trainerProfileRepository.findByUserId(batchProgressRequestDto.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with user id: " + batchProgressRequestDto.getTrainerId()));

        BatchProgress batchProgress = new BatchProgress();
        batchProgress.setTitle(batchProgressRequestDto.getTitle());
        batchProgress.setDescription(batchProgressRequestDto.getDescription());
        batchProgress.setSessionNumber(batchProgressRequestDto.getSessionNumber());
        batchProgress.setTopicCovered(batchProgressRequestDto.getTopicCovered());
        batchProgress.setBatch(batch);
        batchProgress.setTrainer(trainerProfile);

        if (file != null && !file.isEmpty()) {
            java.util.Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
            batchProgress.setDocumentUrl((String) uploadResult.get("secure_url"));
            batchProgress.setDocumentPublicId((String) uploadResult.get("public_id"));
            batchProgress.setDocumentName(file.getOriginalFilename());
        }

        BatchProgress savedProgress = batchProgressRepository.save(batchProgress);
        return mapToBatchProgressResponseDto(savedProgress);
    }

    @Override
    public List<BatchProgressResponseDto> getAllBatchProgress() {
        List<BatchProgress> allProgress = batchProgressRepository.findAll();
        return allProgress.stream()
                .map(this::mapToBatchProgressResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchProgressResponseDto> getBatchProgress(Long batchId) {
        List<BatchProgress> batchProgressList = batchProgressRepository.findByBatchId(batchId);
        return batchProgressList.stream()
                .map(this::mapToBatchProgressResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BatchProgressResponseDto updateBatchProgress(Long id, org.springframework.web.multipart.MultipartFile file, BatchProgressRequestDto batchProgressRequestDto) {
        BatchProgress batchProgress = batchProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch progress not found with id: " + id));

        if (batchProgressRequestDto.getTitle() != null) {
            batchProgress.setTitle(batchProgressRequestDto.getTitle());
        }
        if (batchProgressRequestDto.getDescription() != null) {
            batchProgress.setDescription(batchProgressRequestDto.getDescription());
        }

        if (file != null && !file.isEmpty()) {
            if (batchProgress.getDocumentPublicId() != null) {
                cloudinaryService.deleteFile(batchProgress.getDocumentPublicId());
            }
            java.util.Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
            batchProgress.setDocumentUrl((String) uploadResult.get("secure_url"));
            batchProgress.setDocumentPublicId((String) uploadResult.get("public_id"));
            batchProgress.setDocumentName(file.getOriginalFilename());
        }

        BatchProgress updatedProgress = batchProgressRepository.save(batchProgress);
        return mapToBatchProgressResponseDto(updatedProgress);
    }

    @Override
    public void deleteBatchProgress(Long id) {
        BatchProgress batchProgress = batchProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch progress not found with id: " + id));
        if (batchProgress.getDocumentPublicId() != null) {
            cloudinaryService.deleteFile(batchProgress.getDocumentPublicId());
        }
        batchProgressRepository.delete(batchProgress);
    }

    private BatchProgressResponseDto mapToBatchProgressResponseDto(BatchProgress batchProgress) {
        BatchProgressResponseDto dto = new BatchProgressResponseDto();
        dto.setId(batchProgress.getId());
        dto.setTitle(batchProgress.getTitle());
        dto.setDescription(batchProgress.getDescription());
        dto.setSessionNumber(batchProgress.getSessionNumber());
        dto.setTopicCovered(batchProgress.getTopicCovered());
        if (batchProgress.getBatch() != null) {
            dto.setBatchId(batchProgress.getBatch().getId());
        }
        if (batchProgress.getTrainer() != null && batchProgress.getTrainer().getUser() != null) {
            dto.setTrainerId(batchProgress.getTrainer().getUser().getId());
        }
        dto.setDocumentUrl(batchProgress.getDocumentUrl());
        dto.setDocumentName(batchProgress.getDocumentName());
        dto.setCreatedAt(batchProgress.getCreatedAt());
        return dto;
    }
}
