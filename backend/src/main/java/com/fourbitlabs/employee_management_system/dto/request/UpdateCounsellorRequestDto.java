package com.fourbitlabs.employee_management_system.dto.request;

import com.fourbitlabs.employee_management_system.enums.UserStatus;

import java.time.LocalDate;

public class UpdateCounsellorRequestDto {

    private String name;

    private String phone;

    private String department;

    private LocalDate joiningDate;

    private Double salary;

    private UserStatus userStatus;

    public UpdateCounsellorRequestDto() {
    }

    public UpdateCounsellorRequestDto(String name, String phone, String department,
                                      LocalDate joiningDate, Double salary, UserStatus userStatus) {
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.userStatus = userStatus;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
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
