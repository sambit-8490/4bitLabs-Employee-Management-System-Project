package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.service.interfaces.*;

import com.fourbitlabs.employee_management_system.dto.request.CounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.StudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateCounsellorRequestDto;
import com.fourbitlabs.employee_management_system.dto.request.UpdateStudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.CounsellorResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentResponseDto;
import com.fourbitlabs.employee_management_system.entity.CounsellorProfile;
import com.fourbitlabs.employee_management_system.entity.Student;
import com.fourbitlabs.employee_management_system.entity.User;
import com.fourbitlabs.employee_management_system.enums.Role;
import com.fourbitlabs.employee_management_system.enums.StudentStatus;
import com.fourbitlabs.employee_management_system.enums.UserStatus;
import com.fourbitlabs.employee_management_system.exception.DuplicateResourceException;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.AssignmentRepository;
import com.fourbitlabs.employee_management_system.repository.BatchRepository;
import com.fourbitlabs.employee_management_system.repository.CounsellorProfileRepository;
import com.fourbitlabs.employee_management_system.repository.RefreshTokenRepository;
import com.fourbitlabs.employee_management_system.repository.StudentRepository;
import com.fourbitlabs.employee_management_system.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CounsellorServiceImpl implements CounsellorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounsellorProfileRepository counsellorProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========================
    // Counsellor CRUD
    // ========================

    @Override
    public CounsellorResponseDto createCounsellor(CounsellorRequestDto counsellorRequestDto) {
        // Check for duplicate email
        if (userRepository.existsByEmail(counsellorRequestDto.getEmail())) {
            throw new DuplicateResourceException("A user with email '" + counsellorRequestDto.getEmail() + "' already exists");
        }
        User admin = userRepository.findById(counsellorRequestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("A admin with this id: "+ counsellorRequestDto.getAdminId() + " is not found."));

        User user = new User();
        user.setName(counsellorRequestDto.getName());
        user.setEmail(counsellorRequestDto.getEmail());
        user.setPhone(counsellorRequestDto.getPhone());
        user.setPassword(passwordEncoder.encode(counsellorRequestDto.getPassword()));
        user.setRole(Role.COUNSELLOR);
        user.setStatus(counsellorRequestDto.getUserStatus() != null ? counsellorRequestDto.getUserStatus() : UserStatus.ACTIVE);
        user.setCreatedByAdmin(admin);
        User savedUser = userRepository.save(user);
        user.getManagedUsers().add(savedUser);

        CounsellorProfile counsellorProfile = new CounsellorProfile();
        counsellorProfile.setDepartment(counsellorRequestDto.getDepartment());
        counsellorProfile.setJoiningDate(counsellorRequestDto.getJoiningDate());
        counsellorProfile.setSalary(counsellorRequestDto.getSalary());
        counsellorProfile.setUser(savedUser);
        CounsellorProfile savedCounsellorProfile = counsellorProfileRepository.save(counsellorProfile);

        return mapToResponseDto(savedUser, savedCounsellorProfile);
    }

    @Override
    public List<CounsellorResponseDto> getAllCounsellors() {
        List<User> counsellors = userRepository.findByRole(Role.COUNSELLOR);
        return counsellors.stream()
                .map(user -> {
                    CounsellorProfile profile = counsellorProfileRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Counsellor profile not found for user id: " + user.getId()));
                    return mapToResponseDto(user, profile);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CounsellorResponseDto getCounsellorById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id: " + userId));

        if (user.getRole() != Role.COUNSELLOR) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a counsellor");
        }

        CounsellorProfile profile = counsellorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor profile not found for user id: " + userId));

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public CounsellorResponseDto updateCounsellor(Long userId, UpdateCounsellorRequestDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id: " + userId));

        if (user.getRole() != Role.COUNSELLOR) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a counsellor");
        }

        CounsellorProfile profile = counsellorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor profile not found for user id: " + userId));

        // Update User fields (only non-null values)
        if (updateDto.getName() != null) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        userRepository.save(user);

        // Update CounsellorProfile fields (only non-null values)
        if (updateDto.getDepartment() != null) {
            profile.setDepartment(updateDto.getDepartment());
        }
        if (updateDto.getJoiningDate() != null) {
            profile.setJoiningDate(updateDto.getJoiningDate());
        }
        if (updateDto.getSalary() != null) {
            profile.setSalary(updateDto.getSalary());
        }
        if (updateDto.getUserStatus() != null) {
            user.setStatus(updateDto.getUserStatus());
        }
        counsellorProfileRepository.save(profile);

        return mapToResponseDto(user, profile);
    }

    @Override
    @Transactional
    public void deleteCounsellor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id: " + userId));

        if (user.getRole() != Role.COUNSELLOR) {
            throw new ResourceNotFoundException("User with id " + userId + " is not a counsellor");
        }

        counsellorProfileRepository.findByUserId(userId)
                .ifPresent(profile -> {
                    List<Student> students = studentRepository.findByCounsellorId(profile.getId());
                    if (students != null) {
                        for (Student s : students) {
                            s.setCounsellor(null);
                            studentRepository.save(s);
                        }
                    }

                    counsellorProfileRepository.delete(profile);
                });
        
        refreshTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    // ========================
    // Student CRUD
    // ========================

    @Override
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {
        if (studentRepository.existsByEmail(studentRequestDto.getEmail())) {
            throw new DuplicateResourceException("A student with email '" + studentRequestDto.getEmail() + "' already exists");
        }

        CounsellorProfile counsellorProfile = counsellorProfileRepository.findByUserId(studentRequestDto.getCounsellorId())
                .orElseThrow(() -> new ResourceNotFoundException("A counsellor not found with user id: "+studentRequestDto.getCounsellorId()));

        Student student = new Student();

        student.setName(studentRequestDto.getName());
        student.setEmail(studentRequestDto.getEmail());
        student.setPhone(studentRequestDto.getPhone());
        student.setStatus(StudentStatus.ACTIVE);
        student.setJoiningDate(studentRequestDto.getJoiningDate());
        student.setCounsellor(counsellorProfile);
        Student savedStudent = studentRepository.save(student);

        return getStudentResponseDto(savedStudent);
    }

    @Override
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::getStudentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDto getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        return getStudentResponseDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long studentId, UpdateStudentRequestDto updateDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        // Update Student fields (only non-null values)
        if (updateDto.getName() != null) {
            student.setName(updateDto.getName());
        }
        if (updateDto.getPhone() != null) {
            student.setPhone(updateDto.getPhone());
        }
        if (updateDto.getJoiningDate() != null) {
            student.setJoiningDate(updateDto.getJoiningDate());
        }
        if (updateDto.getStatus() != null) {
            student.setStatus(updateDto.getStatus());
        }
        studentRepository.save(student);

        return getStudentResponseDto(student);
    }

    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<com.fourbitlabs.employee_management_system.entity.Assignment> assignments = assignmentRepository.findByStudentId(studentId);
        if (assignments != null) {
            for (com.fourbitlabs.employee_management_system.entity.Assignment assignment : assignments) {
                com.fourbitlabs.employee_management_system.entity.Batch batch = assignment.getBatch();
                if (batch != null && com.fourbitlabs.employee_management_system.enums.AssignmentStatus.ACTIVE.equals(assignment.getStatus())) {
                    batch.setStudentCount(Math.max(0, (batch.getStudentCount() != null ? batch.getStudentCount() : 0) - 1));
                    batchRepository.save(batch);
                }
            }
            assignmentRepository.deleteAll(assignments);
        }

        studentRepository.delete(student);
    }

    // ========================
    // Mapping Helpers
    // ========================

    @NotNull
    private StudentResponseDto getStudentResponseDto(Student savedStudent) {
        StudentResponseDto studentResponseDto = new StudentResponseDto();
        studentResponseDto.setId(savedStudent.getId());
        studentResponseDto.setName(savedStudent.getName());
        studentResponseDto.setEmail(savedStudent.getEmail());
        studentResponseDto.setPhone(savedStudent.getPhone());
        studentResponseDto.setStatus(savedStudent.getStatus());
        studentResponseDto.setJoiningDate(savedStudent.getJoiningDate());
        studentResponseDto.setCounsellorId(savedStudent.getCounsellor() != null && savedStudent.getCounsellor().getUser() != null ? savedStudent.getCounsellor().getUser().getId() : null);
        return studentResponseDto;
    }

    @NotNull
    private static CounsellorResponseDto mapToResponseDto(User savedUser, CounsellorProfile savedCounsellorProfile) {
        CounsellorResponseDto counsellorResponseDto = new CounsellorResponseDto();
        counsellorResponseDto.setId(savedUser.getId());
        counsellorResponseDto.setName(savedUser.getName());
        counsellorResponseDto.setEmail(savedUser.getEmail());
        counsellorResponseDto.setPhone(savedUser.getPhone());
        counsellorResponseDto.setRole(savedUser.getRole());
        counsellorResponseDto.setStatus(savedUser.getStatus());

        counsellorResponseDto.setDepartment(savedCounsellorProfile.getDepartment());
        counsellorResponseDto.setJoiningDate(savedCounsellorProfile.getJoiningDate());
        counsellorResponseDto.setSalary(savedCounsellorProfile.getSalary());
        return counsellorResponseDto;
    }
}
