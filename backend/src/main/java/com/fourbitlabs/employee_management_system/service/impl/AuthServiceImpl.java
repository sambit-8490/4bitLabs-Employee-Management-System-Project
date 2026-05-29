package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.entity.RefreshToken;
import com.fourbitlabs.employee_management_system.repository.RefreshTokenRepository;
import com.fourbitlabs.employee_management_system.dto.request.LoginRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.LoginResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.TokenRefreshResponseDto;
import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.exception.BadCredentialsException;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.UserRepository;
import com.fourbitlabs.employee_management_system.security.JWTService;
import com.fourbitlabs.employee_management_system.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Users with this email is invalid."));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Users credentials are invalid.");
        }

        // Revoke all previous tokens for this user upon new login
        refreshTokenRepository.revokeAllByUser(user);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshTokenString = jwtService.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken(refreshTokenString, user);
        refreshTokenRepository.save(refreshToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUserId(user.getId());
        loginResponseDto.setName(user.getName());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setRole(user.getRole());
        loginResponseDto.setStatus(user.getStatus());
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshTokenString);

        return loginResponseDto;
    }

    @Override
    @Transactional
    public TokenRefreshResponseDto refreshToken(String refreshToken) {
        
        try {
            RefreshToken dbToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found in database"));

            if (dbToken.isRevoked()) {
                throw new BadCredentialsException("Refresh token has been revoked.");
            }

            String email = jwtService.extractEmail(refreshToken);

            if (email != null) {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
                // Extra security check: does the DB token actually match the user inside the payload?
                if (!dbToken.getUser().getId().equals(user.getId())) {
                    dbToken.setRevoked(true);
                    refreshTokenRepository.save(dbToken);
                    throw new BadCredentialsException("Token compromised");
                }
                
                if (jwtService.isRefreshTokenValid(refreshToken, user.getEmail())) {
                    String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
                    String newRefreshTokenString = jwtService.generateRefreshToken(user.getEmail());
                    
                    dbToken.setRevoked(true);
                    refreshTokenRepository.save(dbToken);

                    RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenString, user);
                    refreshTokenRepository.save(newRefreshToken);

                    return new TokenRefreshResponseDto(newAccessToken, newRefreshTokenString);
                } else {
                    dbToken.setRevoked(true);
                    refreshTokenRepository.save(dbToken);
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid or expired refresh token: " + e.getMessage());
        }
        
        throw new BadCredentialsException("Invalid refresh token");
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null) {

            RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }
}
