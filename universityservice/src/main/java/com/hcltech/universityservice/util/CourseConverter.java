package com.hcltech.universityservice.util;

import org.springframework.stereotype.Component;

import com.hcltech.universityservice.dto.CourseRequestDTO;
import com.hcltech.universityservice.dto.CourseResponseDTO;
import com.hcltech.universityservice.entity.Course;

@Component
public class CourseConverter {

	public Course toEntity(CourseRequestDTO courseRequestDTO) {
		Course.CourseBuilder course = Course.builder()
				.name(courseRequestDTO.getName())
				.description(courseRequestDTO.getDescription())
				.level(courseRequestDTO.getLevel())
				.duration(courseRequestDTO.getDuration())
				.language(courseRequestDTO.getLanguage())
				.price(courseRequestDTO.getPrice())
				.currency(courseRequestDTO.getCurrency());
		return course.build();
	}

	public CourseResponseDTO toResponseDTO(Course course) {
		CourseResponseDTO.CourseResponseDTOBuilder courseResponse = CourseResponseDTO.builder()
				.id(course.getId())
				.name(course.getName())
				.description(course.getDescription())
				.level(course.getLevel())
				.duration(course.getDuration())
				.university(course.getUniversity().getId())
				.language(course.getLanguage())
				.price(course.getPrice())
				.currency(course.getCurrency())
				.createdAt(course.getCreatedAt())
				.updatedAt(course.getUpdatedAt());
		return courseResponse.build();
	}
	
}
