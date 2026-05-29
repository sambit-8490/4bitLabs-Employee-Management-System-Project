package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.BatchStatus;

import java.time.LocalDate;

public class BatchResponseDto {
    private Long id;

    private String name;

    private String course;

    private LocalDate startDate;

    private LocalDate endDate;

    private BatchStatus status;

    private Integer studentCount;

    private Long trainerId;

    private Long analystId;

    public BatchResponseDto() {
    }

    public BatchResponseDto(Long id, String name, String course, LocalDate startDate, LocalDate endDate, BatchStatus status, Integer studentCount,Long trainerId, Long analystId) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.trainerId = trainerId;
        this.analystId = analystId;
        this.studentCount = studentCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Long getAnalystId() {
        return analystId;
    }

    public void setAnalystId(Long analystId) {
        this.analystId = analystId;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }
}
