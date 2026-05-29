package com.fourbitlabs.employee_management_system.entity;

import com.fourbitlabs.employee_management_system.enums.BatchStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String course;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer studentCount = 0;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerProfile trainer;

    @ManyToOne
    @JoinColumn(name = "analyst_id")
    private AnalystProfile analyst;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Batch() {
    }

    public Batch(Long id, String name, String course, LocalDate startDate, LocalDate endDate,
                 BatchStatus status, TrainerProfile trainer, AnalystProfile analyst) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.trainer = trainer;
        this.analyst = analyst;
    }

    public Batch(Long id, String name, String course, LocalDate startDate, LocalDate endDate, Integer studentCount, BatchStatus status, TrainerProfile trainer, AnalystProfile analyst) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentCount = studentCount;
        this.status = status;
        this.trainer = trainer;
        this.analyst = analyst;
    }

    // Method to be called before a new entity is persisted
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Method to be called before an existing entity is updated
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public TrainerProfile getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerProfile trainer) {
        this.trainer = trainer;
    }

    public AnalystProfile getAnalyst() {
        return analyst;
    }

    public void setAnalyst(AnalystProfile analyst) {
        this.analyst = analyst;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }
}
