package com.fourbitlabs.employee_management_system.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TokenRefreshResponseDto {
    private String accessToken;
    
    @JsonIgnore
    private String refreshToken;

    public TokenRefreshResponseDto() {
    }

    public TokenRefreshResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
