package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;

import java.time.LocalDate;

public class CounsellorResponseDto extends UserResponseDto{
    private String department;

    private LocalDate joiningDate;

    private Double salary;

    public CounsellorResponseDto() {
    }

    public CounsellorResponseDto(Long id, String name, String email, String phone, Role role, UserStatus status, String department, LocalDate joiningDate, Double salary) {
        super(id, name, email, phone, role, status);
        this.department = department;
        this.joiningDate = joiningDate;
        this.salary = salary;
    }

    public CounsellorResponseDto(String department, LocalDate joiningDate, Double salary) {
        this.department = department;
        this.joiningDate = joiningDate;
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
