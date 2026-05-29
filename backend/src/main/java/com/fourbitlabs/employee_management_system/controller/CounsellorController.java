package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.request.StudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateStudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.CounsellorResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.CounsellorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CounsellorController handles all counsellor-related operations.
 *
 * The counsellor is responsible for:
 * - Creating and managing students
 */
@RestController
@RequestMapping("/api/counsellor")
public class CounsellorController {

    @Autowired
    private CounsellorService counsellorService;

    // ========================
    // Profile Management
    // ========================

    /**
     * Get counsellor profile by User ID.
     * GET /api/counsellor/profile/{userId}
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<CounsellorResponseDto>> getCounsellorProfile(@PathVariable("userId") Long userId) {
        CounsellorResponseDto counsellorResponseDto = counsellorService.getCounsellorById(userId);
        ApiResponse<CounsellorResponseDto> response = new ApiResponse<>(200, "Counsellor profile fetched successfully", counsellorResponseDto);
        return ResponseEntity.ok(response);
    }

    // ========================
    // Student Management
    // ========================

    /**
     * Create a new student under a counsellor.
     * POST /api/counsellor/students
     */
    @PostMapping("/students")
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(
            @Valid @RequestBody StudentRequestDto studentRequestDto) {
        StudentResponseDto studentResponseDto = counsellorService.createStudent(studentRequestDto);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(201, "Student created successfully", studentResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all students.
     * GET /api/counsellor/students
     */
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents() {
        List<StudentResponseDto> students = counsellorService.getAllStudents();
        ApiResponse<List<StudentResponseDto>> response = new ApiResponse<>(200, "Students fetched successfully", students);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a student by ID.
     * GET /api/counsellor/students/{id}
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable("id") Long id) {
        StudentResponseDto studentResponseDto = counsellorService.getStudentById(id);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(200, "Student fetched successfully", studentResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a student by ID.
     * PUT /api/counsellor/students/{id}
     */
    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateStudentRequestDto updateDto) {
        StudentResponseDto studentResponseDto = counsellorService.updateStudent(id, updateDto);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(200, "Student updated successfully", studentResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft delete a student by ID (sets status to DROPPED).
     * DELETE /api/counsellor/students/{id}
     */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable("id") Long id) {
        counsellorService.deleteStudent(id);
        ApiResponse<Void> response = new ApiResponse<>(200, "Student deactivated successfully", null);
        return ResponseEntity.ok(response);
    }
}
