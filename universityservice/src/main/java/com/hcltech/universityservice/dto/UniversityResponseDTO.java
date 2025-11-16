package com.hcltech.universityservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UniversityResponseDTO {

    private UUID id;

    private String name;

    private String city;
    
    private String state;

    private String country;

    private List<CourseResponseDTO> courses;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
