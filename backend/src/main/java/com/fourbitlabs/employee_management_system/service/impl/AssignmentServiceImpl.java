package com.fourbitlabs.employee_management_system.service.impl;

import com.fourbitlabs.employee_management_system.dto.request.AssignStudentRequestDto;
import com.fourbitlabs.employee_management_system.dto.response.AssignmentResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.AssignmentTransferBatchResponseDto;
import com.fourbitlabs.employee_management_system.dto.response.StudentCourseResponseDto;
import com.fourbitlabs.employee_management_system.entity.Assignment;
import com.fourbitlabs.employee_management_system.entity.Batch;
import com.fourbitlabs.employee_management_system.entity.Student;
import com.fourbitlabs.employee_management_system.enums.AssignmentStatus;
import com.fourbitlabs.employee_management_system.exception.DuplicateResourceException;
import com.fourbitlabs.employee_management_system.exception.ResourceNotFoundException;
import com.fourbitlabs.employee_management_system.repository.AssignmentRepository;
import com.fourbitlabs.employee_management_system.repository.BatchRepository;
import com.fourbitlabs.employee_management_system.repository.StudentRepository;
import com.fourbitlabs.employee_management_system.service.interfaces.AssignmentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BatchRepository batchRepository;

    @Override
    public AssignmentResponseDto assignStudent(AssignStudentRequestDto studentRequestDto) {

        Student student = studentRepository.findById(studentRequestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "A student with this id: " + studentRequestDto.getStudentId() + " is not found."));

        Batch batch = batchRepository.findById(studentRequestDto.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "A batch with this id: " + studentRequestDto.getBatchId() + " is not found."));

        Assignment assignment = new Assignment();
        assignment.setStudent(student);
        assignment.setBatch(batch);
        assignment.setAssignedDate(studentRequestDto.getAssignedDate());
        assignment.setStatus(AssignmentStatus.ACTIVE);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        
        batch.setStudentCount((batch.getStudentCount() != null ? batch.getStudentCount() : 0) + 1);
        batchRepository.save(batch);

        return getAssignmentResponseDto(savedAssignment);
    }

    @Override
    public AssignmentTransferBatchResponseDto transferBatch(AssignStudentRequestDto studentRequestDto) {
        Student student = studentRepository.findById(studentRequestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "A student with this id: " + studentRequestDto.getStudentId() + " is not found."));

        Batch newBatch = batchRepository.findById(studentRequestDto.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "A batch with this id: " + studentRequestDto.getBatchId() + " is not found."));

        Assignment assignment = assignmentRepository.findByStudentIdAndStatus(student.getId(), AssignmentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        if (Objects.equals(assignment.getBatch().getId(), newBatch.getId())) {
            throw new DuplicateResourceException("Student cannot be transferred to the same batch");
        }

        assignment.setStatus(AssignmentStatus.TRANSFERRED);

        Assignment newAssignment = new Assignment();
        newAssignment.setBatch(newBatch);
        newAssignment.setStudent(student);
        newAssignment.setStatus(AssignmentStatus.ACTIVE);
        newAssignment.setAssignedDate(LocalDate.now());
        Assignment newSavedAssignment = assignmentRepository.save(newAssignment);

        Batch oldBatch = assignment.getBatch();
        oldBatch.setStudentCount(Math.max(0, (oldBatch.getStudentCount() != null ? oldBatch.getStudentCount() : 0) - 1));
        batchRepository.save(oldBatch);
        
        newBatch.setStudentCount((newBatch.getStudentCount() != null ? newBatch.getStudentCount() : 0) + 1);
        batchRepository.save(newBatch);

        return getAssignmentTransferBatchResponseDto(newSavedAssignment, assignment);
    }

    @Override
    public List<StudentCourseResponseDto> fetchAllStudentByBatch(Long batchId) {
        List<Student> allStudents = new ArrayList<>();
        List<Assignment> allAssigns = assignmentRepository.findByBatchIdAndStatus(batchId, AssignmentStatus.ACTIVE)
                .stream().distinct().toList();
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("batch not found"));
        Iterator<Assignment> iterator = allAssigns.iterator();
        while (iterator.hasNext()) {
            Assignment assignment = iterator.next();
            Long studentId = assignment.getStudent().getId();
            Student batchStudent = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            allStudents.add(batchStudent);
        }
        return allStudents.stream().map(e -> {
            StudentCourseResponseDto studentResponseDto = new StudentCourseResponseDto();
            studentResponseDto.setId(e.getId());
            studentResponseDto.setName(e.getName());
            studentResponseDto.setPhone(e.getPhone());
            studentResponseDto.setStatus(e.getStatus());
            studentResponseDto.setEmail(e.getEmail());
            studentResponseDto.setCounsellorId(e.getCounsellor().getId());
            studentResponseDto.setJoiningDate(e.getJoiningDate());
            studentResponseDto.setBatchId(batch.getId());
            studentResponseDto.setBatchName(batch.getName());
            studentResponseDto.setCourse(batch.getCourse());
            return studentResponseDto;
        }).toList().stream().distinct().toList();
    }

    @Override
    public List<AssignmentResponseDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(AssignmentServiceImpl::getAssignmentResponseDto)
                .toList();
    }

    @NotNull
    private static AssignmentTransferBatchResponseDto getAssignmentTransferBatchResponseDto(
            Assignment newSavedAssignment, Assignment assignment) {
        AssignmentTransferBatchResponseDto assignmentTransferBatchResponseDto = new AssignmentTransferBatchResponseDto();

        assignmentTransferBatchResponseDto.setId(newSavedAssignment.getId());
        assignmentTransferBatchResponseDto.setNewBatchId(newSavedAssignment.getBatch().getId());
        assignmentTransferBatchResponseDto.setNewBatchName(newSavedAssignment.getBatch().getName());
        assignmentTransferBatchResponseDto.setStudentId(newSavedAssignment.getStudent().getId());
        assignmentTransferBatchResponseDto.setStudentName(newSavedAssignment.getStudent().getName());
        assignmentTransferBatchResponseDto.setStatus(newSavedAssignment.getStatus());
        assignmentTransferBatchResponseDto.setAssignedDate(newSavedAssignment.getAssignedDate());
        assignmentTransferBatchResponseDto.setUpdatedAt(newSavedAssignment.getUpdatedAt());
        assignmentTransferBatchResponseDto.setOldBatchId(assignment.getBatch().getId());
        assignmentTransferBatchResponseDto.setOldBatchName(assignment.getBatch().getName());
        return assignmentTransferBatchResponseDto;
    }

    @NotNull
    private static AssignmentResponseDto getAssignmentResponseDto(Assignment savedAssignment) {
        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();

        assignmentResponseDto.setId(savedAssignment.getId());
        assignmentResponseDto.setBatchId(savedAssignment.getBatch().getId());
        assignmentResponseDto.setBatchName(savedAssignment.getBatch().getName());
        assignmentResponseDto.setStudentId(savedAssignment.getStudent().getId());
        assignmentResponseDto.setStudentName(savedAssignment.getStudent().getName());
        assignmentResponseDto.setStatus(savedAssignment.getStatus());
        assignmentResponseDto.setAssignedDate(savedAssignment.getAssignedDate());
        assignmentResponseDto.setUpdatedAt(savedAssignment.getUpdatedAt());
        return assignmentResponseDto;
    }
}
