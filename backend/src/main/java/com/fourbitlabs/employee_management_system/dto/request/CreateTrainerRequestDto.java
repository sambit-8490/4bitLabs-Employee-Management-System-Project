package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class CreateTrainerRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String specialization;
    private Integer experienceYears;
    private String qualification;
    private Double salary;
    private LocalDate joiningDate;
    private Long adminId;

    public CreateTrainerRequestDto() {
    }

    public CreateTrainerRequestDto(String name, String email, String password, String phone,
                                   String specialization, Integer experienceYears,
                                   String qualification, Double salary,
                                   LocalDate joiningDate, Long adminId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
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
