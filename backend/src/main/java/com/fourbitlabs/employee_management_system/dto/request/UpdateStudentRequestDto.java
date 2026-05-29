package com.fourbitlabs.employee_management_system.dto.request;

import com.fourbitlabs.employee_management_system.enums.StudentStatus;

import java.time.LocalDate;

public class UpdateStudentRequestDto {

    private String name;

    private String phone;

    private LocalDate joiningDate;

    private StudentStatus status;

    public UpdateStudentRequestDto() {
    }

    public UpdateStudentRequestDto(String name, String phone, LocalDate joiningDate, StudentStatus status) {
        this.name = name;
        this.phone = phone;
        this.joiningDate = joiningDate;
        this.status = status;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
