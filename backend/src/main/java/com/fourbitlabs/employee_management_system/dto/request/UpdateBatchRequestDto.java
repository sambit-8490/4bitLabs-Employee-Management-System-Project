package com.fourbitlabs.employee_management_system.dto.request;

import com.fourbitlabs.employee_management_system.enums.BatchStatus;

import java.time.LocalDate;

public class UpdateBatchRequestDto {

    private String name;

    private String course;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long trainerId;

    private Long analystId;

    private BatchStatus status;

    public UpdateBatchRequestDto() {
    }

    public UpdateBatchRequestDto(String name, String course, LocalDate startDate,
                                 LocalDate endDate, Long trainerId, Long analystId, BatchStatus status) {
        this.name = name;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainerId = trainerId;
        this.analystId = analystId;
        this.status = status;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
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
}
