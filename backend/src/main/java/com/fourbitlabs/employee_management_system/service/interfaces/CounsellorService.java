package com.fourbitlabs.employee_management_system.service.interfaces;

import com.fourbitlabs.employee_management_system.dto.request.CounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.StudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateCounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateStudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.CounsellorResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentResponseDto;

import java.util.List;

public interface CounsellorService {
    CounsellorResponseDto createCounsellor(CounsellorRequestDto counsellorRequestDto);
    List<CounsellorResponseDto> getAllCounsellors();
    CounsellorResponseDto getCounsellorById(Long userId);
    CounsellorResponseDto updateCounsellor(Long userId, UpdateCounsellorRequestDto updateDto);
    void deleteCounsellor(Long userId);

    StudentResponseDto createStudent(StudentRequestDto studentRequestDto);
    List<StudentResponseDto> getAllStudents();
    StudentResponseDto getStudentById(Long studentId);
    StudentResponseDto updateStudent(Long studentId, UpdateStudentRequestDto updateDto);
    void deleteStudent(Long studentId);
}

