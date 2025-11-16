package com.hcltech.universityservice.util;

import com.hcltech.universityservice.dto.UniversityRequestDTO;
import com.hcltech.universityservice.dto.UniversityResponseDTO;
import com.hcltech.universityservice.entity.University;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniversityConverter {
	
	@Autowired
	@Lazy
	private CourseConverter courseConverter;

    public University toEntity(UniversityRequestDTO dto) {
        return University.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .build();
    }

    public UniversityResponseDTO toResponseDTO(University university) {
        return UniversityResponseDTO.builder()
                .id(university.getId())
                .name(university.getName())
                .city(university.getCity())
                .state(university.getState())
                .country(university.getCountry())
                .courses(university.getCourses().stream().map(courseConverter::toResponseDTO).toList())
                .createdAt(university.getCreatedAt())
                .updatedAt(university.getUpdatedAt())
                .build();
    }
}