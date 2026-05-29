package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class StudentRequestDto {
    private String name;

    private String email;

    private String phone;

    private LocalDate joiningDate;

    private Long counsellorId;

    public StudentRequestDto() {
    }

    public StudentRequestDto(String name, String email, String phone, LocalDate joiningDate, Long counsellorId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.joiningDate = joiningDate;
        this.counsellorId = counsellorId;
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
