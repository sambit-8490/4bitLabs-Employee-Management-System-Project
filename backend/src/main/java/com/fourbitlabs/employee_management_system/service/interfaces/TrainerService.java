package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.BatchProgressRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.TrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateTrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.BatchProgressResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.TrainerResponseDto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface TrainerService {
    TrainerResponseDto createTrainer(TrainerRequestDto trainerRequestDto);
    List<TrainerResponseDto> getAllTrainers();
    TrainerResponseDto getTrainerById(Long userId);
    TrainerResponseDto updateTrainer(Long userId, UpdateTrainerRequestDto updateDto);
    void deleteTrainer(Long userId);
    BatchProgressResponseDto addBatchProgress(MultipartFile file, BatchProgressRequestDto batchProgressRequestDto);
    List<BatchProgressResponseDto> getAllBatchProgress();
    List<BatchProgressResponseDto> getBatchProgress(Long batchId);
    BatchProgressResponseDto updateBatchProgress(Long id, MultipartFile file, BatchProgressRequestDto batchProgressRequestDto);
    void deleteBatchProgress(Long id);
}
