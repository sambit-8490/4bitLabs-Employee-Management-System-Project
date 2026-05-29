package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.response.TrainerResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TrainerController handles all trainer-related operations.
 * 
 * Note: Trainer creation is handled by the AdminController.
 * Note: Batch progress operations have been moved to BatchProgressController.
 */
@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    /**
     * Get trainer profile by User ID.
     * GET /api/trainer/profile/{userId}
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<TrainerResponseDto>> getTrainerProfile(@PathVariable("userId") Long userId) {
        TrainerResponseDto trainerResponseDto = trainerService.getTrainerById(userId);
        ApiResponse<TrainerResponseDto> response = new ApiResponse<>(200, "Trainer profile fetched successfully", trainerResponseDto);
        return ResponseEntity.ok(response);
    }
}
