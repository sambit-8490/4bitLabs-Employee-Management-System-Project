package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.LoginRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.LoginResponseDto;

import com.fourbitlabs.employee_management_system.dto.response.TokenRefreshResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    TokenRefreshResponseDto refreshToken(String refreshToken);

    void logout(String refreshToken);
}
