package com.fourbitlabs.employee_management_system.controller;

import com.fourbitlabs.employee_management_system.dto.response.TokenRefreshResponseDto;

import com.fourbitlabs.employee_management_system.dto.request.LoginRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.LoginResponseDto;
import com.fourbitlabs.employee_management_system.response.ApiResponse;
import com.fourbitlabs.employee_management_system.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days in seconds

    @Autowired
    private AuthService authService;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site:Lax}")
    private String cookieSameSite;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        // Set HttpOnly refresh token cookie
        addRefreshCookie(response, loginResponseDto.getRefreshToken(), REFRESH_COOKIE_MAX_AGE);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Login successful", loginResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken, HttpServletResponse response){
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Refresh Token is missing in cookies", null));
        }

        TokenRefreshResponseDto responseDto = authService.refreshToken(refreshToken);

        // Rotate the refresh token cookie
        addRefreshCookie(response, responseDto.getRefreshToken(), REFRESH_COOKIE_MAX_AGE);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Token refreshed successfully", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            authService.logout(refreshToken);
        }

        // Clear the refresh token cookie by setting maxAge to 0
        addRefreshCookie(response, "", 0);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Logout successful", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * Creates a properly configured refresh token cookie using Spring's ResponseCookie.
     * This ensures:
     * - HttpOnly: cookie is not accessible via JavaScript (XSS protection)
     * - Path "/": cookie is sent on ALL requests, not just /api/auth
     * - SameSite: configurable per environment (Lax for local, None for cross-origin prod)
     * - Secure: configurable per environment (false for HTTP local, true for HTTPS prod)
     */
    private void addRefreshCookie(HttpServletResponse response, String tokenValue, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, tokenValue)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
