package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.request.AssignStudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AssignmentResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.AssignmentTransferBatchResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentCourseResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AssignmentController handles student-to-batch assignment operations.
 * 
 * Responsibilities:
 * - Assign a student to a batch
 * - Transfer a student between batches
 * - Get all students in a specific batch
 * 
 * Note: Assignment service methods are not yet implemented.
 *       Endpoints will be added here once the service layer supports them.
 * 
 * Planned endpoints:
 * - POST /api/assignments               → Assign student to batch
 * - PUT  /api/assignments/transfer       → Transfer student to another batch
 * - GET  /api/assignments/batch/{batchId} → Get students in a batch
 */
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    // Assignment endpoints will be added here once
    // the corresponding service methods are available.

    @PostMapping
    public ResponseEntity<?> assignStudentToBatch(@Valid @RequestBody AssignStudentRequestDto studentRequestDto){
        AssignmentResponseDto assignmentResponseDto = assignmentService.assignStudent(studentRequestDto);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "Student assigned!", assignmentResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/transfer")
    public ResponseEntity<?> transferBatch(@Valid @RequestBody AssignStudentRequestDto studentRequestDto){
        AssignmentTransferBatchResponseDto assignmentResponseDto = assignmentService.transferBatch(studentRequestDto);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Batch transferred!", assignmentResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<?> getAllStudentByBatch(@PathVariable("batchId") Long batchId){
        List<StudentCourseResponseDto> allStudents = assignmentService.fetchAllStudentByBatch(batchId);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "All students fetched!", allStudents);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssignmentResponseDto>>> getAllAssignments() {
        List<AssignmentResponseDto> assignments = assignmentService.getAllAssignments();
        ApiResponse<List<AssignmentResponseDto>> apiResponse = new ApiResponse<>(200, "All assignments fetched!", assignments);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
