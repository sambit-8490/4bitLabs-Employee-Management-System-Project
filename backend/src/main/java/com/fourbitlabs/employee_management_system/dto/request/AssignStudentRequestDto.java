package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class AssignStudentRequestDto {
    private Long studentId;
    private Long batchId;
    private LocalDate assignedDate;

    public AssignStudentRequestDto() {
        this.assignedDate = LocalDate.now();
    }

    public AssignStudentRequestDto(Long studentId, Long batchId, LocalDate assignedDate) {
        this.studentId = studentId;
        this.batchId = batchId;
        this.assignedDate = assignedDate;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}
