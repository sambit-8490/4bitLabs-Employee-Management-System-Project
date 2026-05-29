package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.AdminRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AdminResponseDto;

public interface AdminService {
    AdminResponseDto createAdmin(AdminRequestDto createAdminRequestDto);
}
