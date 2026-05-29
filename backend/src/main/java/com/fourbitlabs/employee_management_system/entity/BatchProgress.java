package com.fourbitlabs.employee_management_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_progress")
public class BatchProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String documentUrl;

    private String documentPublicId;

    private String documentName;

    private Integer sessionNumber;

    private String topicCovered;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerProfile trainer;

    public BatchProgress() {
    }

    public BatchProgress(Long id, String title, String description, String documentName,
                         String documentUrl, String documentPublicId, Integer sessionNumber, String topicCovered,
                         Batch batch, TrainerProfile trainer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
        this.documentPublicId = documentPublicId;
        this.sessionNumber = sessionNumber;
        this.topicCovered = topicCovered;
        this.batch = batch;
        this.trainer = trainer;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getDocumentPublicId() {
        return documentPublicId;
    }

    public void setDocumentPublicId(String documentPublicId) {
        this.documentPublicId = documentPublicId;
    }

    public Integer getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(Integer sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public String getTopicCovered() {
        return topicCovered;
    }

    public void setTopicCovered(String topicCovered) {
        this.topicCovered = topicCovered;
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

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public TrainerProfile getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerProfile trainer) {
        this.trainer = trainer;
    }
}
