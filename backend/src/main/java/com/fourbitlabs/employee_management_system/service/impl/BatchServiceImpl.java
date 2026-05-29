package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.repository.TrainerProfileRepository;
import com.fourbitlabs.employee_management_system.service.interfaces.*;

import com.fourbitlabs.employee_management_system.dto.request.BatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateBatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.BatchResponseDto;
import com.fourbitlabs.employee_management_system.entity.AnalystProfile;
import com.fourbitlabs.employee_management_system.entity.Batch;
import com.fourbitlabs.employee_management_system.entity.TrainerProfile;
import com.fourbitlabs.employee_management_system.enums.BatchStatus;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.AnalystProfileRepository;
import com.fourbitlabs.employee_management_system.repository.AssignmentRepository;
import com.fourbitlabs.employee_management_system.repository.BatchProgressRepository;
import com.fourbitlabs.employee_management_system.repository.BatchRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private AnalystProfileRepository analystProfileRepository;

    @Autowired
    private TrainerProfileRepository trainerProfileRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BatchProgressRepository batchProgressRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public BatchResponseDto saveBatch(BatchRequestDto batchRequestDto) {
        AnalystProfile analystProfile = analystProfileRepository.findByUserId(batchRequestDto.getAnalystId())
                .orElseThrow(() -> new ResourceNotFoundException("Analyst not found with user id: " + batchRequestDto.getAnalystId()));

        TrainerProfile trainerProfile = trainerProfileRepository.findByUserId(batchRequestDto.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with user id: " + batchRequestDto.getTrainerId()));

        Batch batch = new Batch();
        batch.setName(batchRequestDto.getName());
        batch.setCourse(batchRequestDto.getCourse());
        batch.setStartDate(batchRequestDto.getStartDate());
        batch.setEndDate(batchRequestDto.getEndDate());
        batch.setAnalyst(analystProfile);
        batch.setTrainer(trainerProfile);
        batch.setStatus(BatchStatus.ACTIVE);
        batch.setStudentCount(batch.getStudentCount());
        Batch savedBatch = batchRepository.save(batch);

        return getBatchResponseDto(savedBatch);
    }

    @Override
    public List<BatchResponseDto> getAllBatches() {
        List<Batch> batches = batchRepository.findAll();
        return batches.stream()
                .map(BatchServiceImpl::getBatchResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BatchResponseDto getBatchById(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));

        return getBatchResponseDto(batch);
    }

    @Override
    @Transactional
    public BatchResponseDto updateBatch(Long batchId, UpdateBatchRequestDto updateDto) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));

        // Update Batch fields (only non-null values)
        if (updateDto.getName() != null) {
            batch.setName(updateDto.getName());
        }
        if (updateDto.getCourse() != null) {
            batch.setCourse(updateDto.getCourse());
        }
        if (updateDto.getStartDate() != null) {
            batch.setStartDate(updateDto.getStartDate());
        }
        if (updateDto.getEndDate() != null) {
            batch.setEndDate(updateDto.getEndDate());
        }

        // Allow reassigning trainer
        if (updateDto.getTrainerId() != null) {
            TrainerProfile trainerProfile = trainerProfileRepository.findByUserId(updateDto.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with user id: " + updateDto.getTrainerId()));
            batch.setTrainer(trainerProfile);
        }

        // Allow reassigning analyst
        if (updateDto.getAnalystId() != null) {
            AnalystProfile analystProfile = analystProfileRepository.findByUserId(updateDto.getAnalystId())
                    .orElseThrow(() -> new ResourceNotFoundException("Analyst not found with user id: " + updateDto.getAnalystId()));
            batch.setAnalyst(analystProfile);
        }

        if (updateDto.getStatus() != null) {
            batch.setStatus(updateDto.getStatus());
        }

        batchRepository.save(batch);
        return getBatchResponseDto(batch);
    }

    @Override
    @Transactional
    public void deleteBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));

        List<com.fourbitlabs.employee_management_system.entity.Assignment> assignments = assignmentRepository.findByBatchId(batchId);
        if (assignments != null) {
            assignmentRepository.deleteAll(assignments);
        }

        List<com.fourbitlabs.employee_management_system.entity.BatchProgress> progresses = batchProgressRepository.findByBatchId(batchId);
        if (progresses != null) {
            for (com.fourbitlabs.employee_management_system.entity.BatchProgress p : progresses) {
                if (p.getDocumentPublicId() != null) {
                    cloudinaryService.deleteFile(p.getDocumentPublicId());
                }
            }
            batchProgressRepository.deleteAll(progresses);
        }

        batchRepository.delete(batch);
    }

    @NotNull
    private static BatchResponseDto getBatchResponseDto(Batch savedBatch) {
        BatchResponseDto batchResponseDto = new BatchResponseDto();
        batchResponseDto.setId(savedBatch.getId());
        batchResponseDto.setName(savedBatch.getName());
        batchResponseDto.setCourse(savedBatch.getCourse());
        batchResponseDto.setStatus(savedBatch.getStatus());
        batchResponseDto.setStartDate(savedBatch.getStartDate());
        batchResponseDto.setEndDate(savedBatch.getEndDate());
        batchResponseDto.setStudentCount(savedBatch.getStudentCount());
        batchResponseDto.setAnalystId(savedBatch.getAnalyst() != null ? savedBatch.getAnalyst().getUser().getId() : null);
        batchResponseDto.setTrainerId(savedBatch.getTrainer() != null ? savedBatch.getTrainer().getUser().getId() : null);
        return batchResponseDto;
    }
}
