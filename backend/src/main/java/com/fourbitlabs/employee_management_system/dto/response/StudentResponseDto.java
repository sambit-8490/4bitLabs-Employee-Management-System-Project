package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.StudentStatus;

import java.time.LocalDate;

public class StudentResponseDto {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private StudentStatus status;

    private LocalDate joiningDate;

    private Long counsellorId;

    public StudentResponseDto() {
    }

    public StudentResponseDto(Long id, String name, String email, String phone, StudentStatus status, LocalDate joiningDate, Long counsellorId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.joiningDate = joiningDate;
        this.counsellorId = counsellorId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getCounsellorId() {
        return counsellorId;
    }

    public void setCounsellorId(Long counsellorId) {
        this.counsellorId = counsellorId;
    }
}
