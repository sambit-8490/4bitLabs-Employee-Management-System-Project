package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;

import java.time.LocalDate;

public class TrainerResponseDto extends UserResponseDto{

    private String specialization;

    private Integer experienceYears;

    private String qualification;

    private LocalDate joiningDate;

    private Double salary;

    public TrainerResponseDto() {
    }

    public TrainerResponseDto(String specialization, Integer experienceYears, String qualification, LocalDate joiningDate, Double salary) {
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.salary = salary;
    }

    public TrainerResponseDto(Long id, String name, String email, String phone, Role role, UserStatus status, String specialization, Integer experienceYears, String qualification, LocalDate joiningDate, Double salary) {
        super(id, name, email, phone, role, status);
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
        this.joiningDate = joiningDate;
        this.salary = salary;
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
}
