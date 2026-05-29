package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.AnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateAnalystRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AnalystResponseDto;

import java.util.List;

public interface AnalystService {
    AnalystResponseDto createAnalyst(AnalystRequestDto analystRequestDto);
    List<AnalystResponseDto> getAllAnalysts();
    AnalystResponseDto getAnalystById(Long userId);
    AnalystResponseDto updateAnalyst(Long userId, UpdateAnalystRequestDto updateDto);
    void deleteAnalyst(Long userId);
}
