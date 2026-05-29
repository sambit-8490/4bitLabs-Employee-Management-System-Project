package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.request.AdminRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.AnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.CounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.TrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateTrainerRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateAnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateCounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AdminResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.AnalystResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.CounsellorResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.TrainerResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.AdminService;
import com.fourbitlabs.employee_management_system.service.interfaces.AnalystService;
import com.fourbitlabs.employee_management_system.service.interfaces.CounsellorService;
import com.fourbitlabs.employee_management_system.service.interfaces.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController handles all admin-level operations.
 * 
 * The admin is responsible for:
 * - Self-registration (creating the admin account)
 * - Creating and managing Trainers, Analysts, and Counsellors
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private AnalystService analystService;

    @Autowired
    private CounsellorService counsellorService;

    // ========================
    // Admin Self-Registration
    // ========================

    /**
     * Register a new admin account.
     * POST /api/admin/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminResponseDto>> registerAdmin(
            @Valid @RequestBody AdminRequestDto adminRequestDto) {
        AdminResponseDto adminResponseDto = adminService.createAdmin(adminRequestDto);
        ApiResponse<AdminResponseDto> response = new ApiResponse<>(201, "Admin registered successfully", adminResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================
    // Trainer Management
    // ========================

    /**
     * Create a new trainer under an admin.
     * POST /api/admin/trainers
     */
    @PostMapping("/trainers")
    public ResponseEntity<ApiResponse<TrainerResponseDto>> createTrainer(
            @Valid @RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto trainerResponseDto = trainerService.createTrainer(trainerRequestDto);
        ApiResponse<TrainerResponseDto> response = new ApiResponse<>(201, "Trainer created successfully", trainerResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all trainers.
     * GET /api/admin/trainers
     */
    @GetMapping("/trainers")
    public ResponseEntity<ApiResponse<List<TrainerResponseDto>>> getAllTrainers() {
        List<TrainerResponseDto> trainers = trainerService.getAllTrainers();
        ApiResponse<List<TrainerResponseDto>> response = new ApiResponse<>(200, "Trainers fetched successfully", trainers);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a trainer by user ID.
     * GET /api/admin/trainers/{id}
     */
    @GetMapping("/trainers/{id}")
    public ResponseEntity<ApiResponse<TrainerResponseDto>> getTrainerById(@PathVariable("id") Long id) {
        TrainerResponseDto trainerResponseDto = trainerService.getTrainerById(id);
        ApiResponse<TrainerResponseDto> response = new ApiResponse<>(200, "Trainer fetched successfully", trainerResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a trainer by user ID.
     * PUT /api/admin/trainers/{id}
     */
    @PutMapping("/trainers/{id}")
    public ResponseEntity<ApiResponse<TrainerResponseDto>> updateTrainer(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateTrainerRequestDto updateDto) {
        TrainerResponseDto trainerResponseDto = trainerService.updateTrainer(id, updateDto);
        ApiResponse<TrainerResponseDto> response = new ApiResponse<>(200, "Trainer updated successfully", trainerResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a trainer by user ID (sets status to INACTIVE).
     * DELETE /api/admin/trainers/{id}
     */
    @DeleteMapping("/trainers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrainer(@PathVariable("id") Long id) {
        trainerService.deleteTrainer(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "Trainer deactivated successfully", null);
        return ResponseEntity.ok(response);
    }

    // ========================
    // Analyst Management
    // ========================

    /**
     * Create a new analyst under an admin.
     * POST /api/admin/analysts
     */
    @PostMapping("/analysts")
    public ResponseEntity<ApiResponse<AnalystResponseDto>> createAnalyst(
            @Valid @RequestBody AnalystRequestDto analystRequestDto) {
        AnalystResponseDto analystResponseDto = analystService.createAnalyst(analystRequestDto);
        ApiResponse<AnalystResponseDto> response = new ApiResponse<>(201, "Analyst created successfully", analystResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all analysts.
     * GET /api/admin/analysts
     */
    @GetMapping("/analysts")
    public ResponseEntity<ApiResponse<List<AnalystResponseDto>>> getAllAnalysts() {
        List<AnalystResponseDto> analysts = analystService.getAllAnalysts();
        ApiResponse<List<AnalystResponseDto>> response = new ApiResponse<>(200, "Analysts fetched successfully", analysts);
        return ResponseEntity.ok(response);
    }

    /**
     * Get an analyst by user ID.
     * GET /api/admin/analysts/{id}
     */
    @GetMapping("/analysts/{id}")
    public ResponseEntity<ApiResponse<AnalystResponseDto>> getAnalystById(@PathVariable("id") Long id) {
        AnalystResponseDto analystResponseDto = analystService.getAnalystById(id);
        ApiResponse<AnalystResponseDto> response = new ApiResponse<>(200, "Analyst fetched successfully", analystResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an analyst by user ID.
     * PUT /api/admin/analysts/{id}
     */
    @PutMapping("/analysts/{id}")
    public ResponseEntity<ApiResponse<AnalystResponseDto>> updateAnalyst(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateAnalystRequestDto updateDto) {
        AnalystResponseDto analystResponseDto = analystService.updateAnalyst(id, updateDto);
        ApiResponse<AnalystResponseDto> response = new ApiResponse<>(200, "Analyst updated successfully", analystResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete an analyst by user ID (sets status to INACTIVE).
     * DELETE /api/admin/analysts/{id}
     */
    @DeleteMapping("/analysts/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnalyst(@PathVariable("id") Long id) {
        analystService.deleteAnalyst(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "Analyst deactivated successfully", null);
        return ResponseEntity.ok(response);
    }

    // ========================
    // Counsellor Management
    // ========================

    /**
     * Create a new counsellor under an admin.
     * POST /api/admin/counsellors
     */
    @PostMapping("/counsellors")
    public ResponseEntity<ApiResponse<CounsellorResponseDto>> createCounsellor(
            @Valid @RequestBody CounsellorRequestDto counsellorRequestDto) {
        CounsellorResponseDto counsellorResponseDto = counsellorService.createCounsellor(counsellorRequestDto);
        ApiResponse<CounsellorResponseDto> response = new ApiResponse<>(201, "Counsellor created successfully", counsellorResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all counsellors.
     * GET /api/admin/counsellors
     */
    @GetMapping("/counsellors")
    public ResponseEntity<ApiResponse<List<CounsellorResponseDto>>> getAllCounsellors() {
        List<CounsellorResponseDto> counsellors = counsellorService.getAllCounsellors();
        ApiResponse<List<CounsellorResponseDto>> response = new ApiResponse<>(200, "Counsellors fetched successfully", counsellors);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a counsellor by user ID.
     * GET /api/admin/counsellors/{id}
     */
    @GetMapping("/counsellors/{id}")
    public ResponseEntity<ApiResponse<CounsellorResponseDto>> getCounsellorById(@PathVariable("id") Long id) {
        CounsellorResponseDto counsellorResponseDto = counsellorService.getCounsellorById(id);
        ApiResponse<CounsellorResponseDto> response = new ApiResponse<>(200, "Counsellor fetched successfully", counsellorResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a counsellor by user ID.
     * PUT /api/admin/counsellors/{id}
     */
    @PutMapping("/counsellors/{id}")
    public ResponseEntity<ApiResponse<CounsellorResponseDto>> updateCounsellor(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateCounsellorRequestDto updateDto) {
        CounsellorResponseDto counsellorResponseDto = counsellorService.updateCounsellor(id, updateDto);
        ApiResponse<CounsellorResponseDto> response = new ApiResponse<>(200, "Counsellor updated successfully", counsellorResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a counsellor by user ID (sets status to INACTIVE).
     * DELETE /api/admin/counsellors/{id}
     */
    @DeleteMapping("/counsellors/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCounsellor(@PathVariable("id") Long id) {
        counsellorService.deleteCounsellor(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "Counsellor deactivated successfully", null);
        return ResponseEntity.ok(response);
    }
}
