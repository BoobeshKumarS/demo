package com.hcltech.studentservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.hcltech.studentservice.dto.StudentRequestDTO;
import com.hcltech.studentservice.dto.StudentResponseDTO;

public interface StudentService {
	StudentResponseDTO registerStudent(StudentRequestDTO studentRequest);
	StudentResponseDTO getStudentById(UUID id);
	List<StudentResponseDTO> getAllStudents();
	StudentResponseDTO updateStudent(UUID id, StudentRequestDTO studentRequest);
	void deleteStudent(UUID id);
	StudentResponseDTO getCurrentStudent(Authentication authentication);
}
