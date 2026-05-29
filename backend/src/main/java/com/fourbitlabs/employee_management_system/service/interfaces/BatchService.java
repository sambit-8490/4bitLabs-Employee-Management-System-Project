package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.BatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateBatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.BatchResponseDto;

import java.util.List;

public interface BatchService {
    BatchResponseDto saveBatch(BatchRequestDto batchRequestDto);
    List<BatchResponseDto> getAllBatches();
    BatchResponseDto getBatchById(Long batchId);
    BatchResponseDto updateBatch(Long batchId, UpdateBatchRequestDto updateDto);
    void deleteBatch(Long batchId);
}

