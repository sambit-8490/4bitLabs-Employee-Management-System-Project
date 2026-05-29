package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.AssignmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentTransferBatchResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long newBatchId;
    private String newBatchName;
    private Long oldBatchId;
    private String oldBatchName;
    private LocalDate assignedDate;
    private AssignmentStatus status;
    private LocalDateTime updatedAt;

    public AssignmentTransferBatchResponseDto() {
    }

    public AssignmentTransferBatchResponseDto(Long id, Long studentId, String studentName, Long newBatchId, String newBatchName, Long oldBatchId, String oldBatchName, LocalDate assignedDate, AssignmentStatus status, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.newBatchId = newBatchId;
        this.newBatchName = newBatchName;
        this.oldBatchId = oldBatchId;
        this.oldBatchName = oldBatchName;
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

    public Long getNewBatchId() {
        return newBatchId;
    }

    public void setNewBatchId(Long newBatchId) {
        this.newBatchId = newBatchId;
    }

    public String getNewBatchName() {
        return newBatchName;
    }

    public void setNewBatchName(String newBatchName) {
        this.newBatchName = newBatchName;
    }

    public Long getOldBatchId() {
        return oldBatchId;
    }

    public void setOldBatchId(Long oldBatchId) {
        this.oldBatchId = oldBatchId;
    }

    public String getOldBatchName() {
        return oldBatchName;
    }

    public void setOldBatchName(String oldBatchName) {
        this.oldBatchName = oldBatchName;
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
