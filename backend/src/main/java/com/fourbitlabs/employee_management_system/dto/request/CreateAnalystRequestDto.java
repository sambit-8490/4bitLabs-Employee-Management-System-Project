package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class CreateAnalystRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String department;
    private Double salary;
    private LocalDate joiningDate;
    private Long adminId;

    public CreateAnalystRequestDto() {
    }

    public CreateAnalystRequestDto(String name, String email, String password, String phone,
                                   String department, Double salary, LocalDate joiningDate, Long adminId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.adminId = adminId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
