package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class AnalystRequestDto extends UserRequestDto{
    private String department;

    private LocalDate joiningDate;

    private Double salary;

    private Long adminId;

    public AnalystRequestDto() {

        this.joiningDate = LocalDate.now();
    }

    public AnalystRequestDto(String department, LocalDate joiningDate, Double salary, Long adminId) {
        this.department = department;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.adminId = adminId;
    }

    public AnalystRequestDto(String name, String email, String password, String phone, String department, LocalDate joiningDate, Double salary, Long adminId) {
        super(name, email, password, phone);
        this.department = department;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.adminId = adminId;
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
