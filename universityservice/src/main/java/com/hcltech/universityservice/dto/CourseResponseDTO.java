package com.hcltech.universityservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hcltech.universityservice.entity.LevelOfEducation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private LevelOfEducation level;
    private int duration;
    private UUID university;
    private String language;
    private double price;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}