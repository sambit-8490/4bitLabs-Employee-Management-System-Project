package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.StudentStatus;

import java.time.LocalDate;

public class StudentCourseResponseDto extends StudentResponseDto{
    private Long batchId;
    private String course;
    private String batchName;

    public StudentCourseResponseDto() {
    }

    public StudentCourseResponseDto(Long batchId, String course, String batchName) {
        this.batchId = batchId;
        this.course = course;
        this.batchName = batchName;
    }

    public StudentCourseResponseDto(Long id, String name, String email, String phone, StudentStatus status, LocalDate joiningDate, Long counsellorId, Long batchId, String course, String batchName) {
        super(id, name, email, phone, status, joiningDate, counsellorId);
        this.batchId = batchId;
        this.course = course;
        this.batchName = batchName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}
