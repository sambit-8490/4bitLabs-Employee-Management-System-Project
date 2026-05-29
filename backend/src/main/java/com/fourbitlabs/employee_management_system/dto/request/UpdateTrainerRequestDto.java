package com.fourbitlabs.employee_management_system.dto.request;

import com.fourbitlabs.employee_management_system.enums.UserStatus;

import java.time.LocalDate;

public class UpdateTrainerRequestDto {

    private String name;

    private String phone;

    private String specialization;

    private Integer experienceYears;

    private String qualification;

    private LocalDate joiningDate;

    private UserStatus userStatus;

    private Double salary;

    public UpdateTrainerRequestDto() {
    }

    public UpdateTrainerRequestDto(String name, String phone, String specialization,
                                   Integer experienceYears, String qualification,
                                   LocalDate joiningDate, Double salary, UserStatus userStatus) {
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
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
