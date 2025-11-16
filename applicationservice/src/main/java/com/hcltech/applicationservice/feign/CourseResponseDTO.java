package com.hcltech.applicationservice.feign;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class CourseResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String courseImage;
    private LevelOfEducation level;
    private String duration;
    private UUID university;
    private String language;
    private String priceRange;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}