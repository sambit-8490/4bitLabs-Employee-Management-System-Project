package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.AssignmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long batchId;
    private String batchName;
    private LocalDate assignedDate;
    private AssignmentStatus status;
    private LocalDateTime updatedAt;

    public AssignmentResponseDto() {
    }

    public AssignmentResponseDto(Long id, Long studentId, String studentName, Long batchId,
                                 String batchName, LocalDate assignedDate,
                                 AssignmentStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.batchId = batchId;
        this.batchName = batchName;
        this.assignedDate = assignedDate;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
