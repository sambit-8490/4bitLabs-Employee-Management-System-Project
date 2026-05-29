package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.request.BatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateBatchRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AnalystResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.BatchResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.AnalystService;
import com.fourbitlabs.employee_management_system.service.interfaces.BatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AnalystController handles all analyst-related operations.
 *
 * The analyst is responsible for:
 * - Creating and managing batches
 */
@RestController
@RequestMapping("/api/analyst")
public class AnalystController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AnalystService analystService;

    // ========================
    // Profile Management
    // ========================

    /**
     * Get analyst profile by User ID.
     * GET /api/analyst/profile/{userId}
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<AnalystResponseDto>> getAnalystProfile(@PathVariable("userId") Long userId) {
        AnalystResponseDto analystResponseDto = analystService.getAnalystById(userId);
        ApiResponse<AnalystResponseDto> response = new ApiResponse<>(200, "Analyst profile fetched successfully", analystResponseDto);
        return ResponseEntity.ok(response);
    }

    // ========================
    // Batch Management
    // ========================

    /**
     * Create a new batch.
     * POST /api/analyst/batches
     */
    @PostMapping("/batches")
    public ResponseEntity<ApiResponse<BatchResponseDto>> createBatch(
            @Valid @RequestBody BatchRequestDto batchRequestDto) {
        BatchResponseDto batchResponseDto = batchService.saveBatch(batchRequestDto);
        ApiResponse<BatchResponseDto> response = new ApiResponse<>(201, "Batch created successfully", batchResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all batches.
     * GET /api/analyst/batches
     */
    @GetMapping("/batches")
    public ResponseEntity<ApiResponse<List<BatchResponseDto>>> getAllBatches() {
        List<BatchResponseDto> batches = batchService.getAllBatches();
        ApiResponse<List<BatchResponseDto>> response = new ApiResponse<>(200, "Batches fetched successfully", batches);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a batch by ID.
     * GET /api/analyst/batches/{id}
     */
    @GetMapping("/batches/{id}")
    public ResponseEntity<ApiResponse<BatchResponseDto>> getBatchById(@PathVariable("id") Long id) {
        BatchResponseDto batchResponseDto = batchService.getBatchById(id);
        ApiResponse<BatchResponseDto> response = new ApiResponse<>(200, "Batch fetched successfully", batchResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a batch by ID.
     * PUT /api/analyst/batches/{id}
     */
    @PutMapping("/batches/{id}")
    public ResponseEntity<ApiResponse<BatchResponseDto>> updateBatch(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateBatchRequestDto updateDto) {
        BatchResponseDto batchResponseDto = batchService.updateBatch(id, updateDto);
        ApiResponse<BatchResponseDto> response = new ApiResponse<>(200, "Batch updated successfully", batchResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a batch by ID (sets status to CANCELLED).
     * DELETE /api/analyst/batches/{id}
     */
    @DeleteMapping("/batches/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBatch(@PathVariable("id") Long id) {
        batchService.deleteBatch(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "Batch cancelled successfully", null);
        return ResponseEntity.ok(response);
    }
}
