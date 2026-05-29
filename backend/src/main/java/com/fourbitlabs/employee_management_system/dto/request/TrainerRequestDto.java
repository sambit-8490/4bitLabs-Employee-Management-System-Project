package com.fourbitlabs.employee_management_system.dto.request;

import java.time.LocalDate;

public class TrainerRequestDto extends UserRequestDto {
    private String specialization;

    private Integer experienceYears;

    private String qualification;

    private LocalDate joiningDate;

    private Double salary;

    private Long adminId;

    public TrainerRequestDto() {
        this.joiningDate = LocalDate.now();
    }

    public TrainerRequestDto(String name, String email, String password, String phone, String specialization, Integer experienceYears, String qualification, LocalDate joiningDate, Double salary, Long adminId) {
        super(name, email, password, phone);
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.adminId = adminId;
    }

    public TrainerRequestDto(String specialization, Integer experienceYears, String qualification, LocalDate joiningDate, Double salary, Long adminId) {
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.adminId = adminId;
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
