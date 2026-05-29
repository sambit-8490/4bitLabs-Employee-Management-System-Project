package com.fourbitlabs.employee_management_system.dto.response;

import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.UserStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginResponseDto {
    private Long userId;

    private String name;

    private String email;

    private Role role;

    private UserStatus status;

    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(Long userId, String name, String email, Role role, UserStatus status, String accessToken, String refreshToken) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
