package com.hcltech.studentservice.util;

import org.springframework.stereotype.Component;

import com.hcltech.studentservice.dto.StudentRequestDTO;
import com.hcltech.studentservice.dto.StudentResponseDTO;
import com.hcltech.studentservice.entity.Student;

@Component
public class StudentConverter {
	public StudentResponseDTO toResponse(Student student) {
		StudentResponseDTO.StudentResponseDTOBuilder builder =  StudentResponseDTO.builder()
				.id(student.getId())
				.firstName(student.getFirstName())
				.lastName(student.getLastName())
				.email(student.getEmail())
				.dateOfBirth(student.getDateOfBirth())
				.gender(student.getGender().getDisplayName())
				.phoneNumber(student.getPhoneNumber())
				.nationality(student.getNationality())
				.address(student.getAddress())
				.createdAt(student.getCreatedAt())
				.updatedAt(student.getUpdatedAt());
		return builder.build();
	}
	
	public Student toEntity(StudentRequestDTO studentRequest) {
		Student.StudentBuilder student = Student.builder()
				.firstName(studentRequest.getFirstName())
				.lastName(studentRequest.getLastName())
				.email(studentRequest.getEmail())
				.dateOfBirth(studentRequest.getDateOfBirth())
				.phoneNumber(studentRequest.getPhoneNumber())
				.gender(studentRequest.getGender())
				.nationality(studentRequest.getNationality())
				.address(studentRequest.getAddress());
		
		return student.build();
	}
}
