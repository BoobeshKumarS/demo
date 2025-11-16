package com.hcltech.applicationservice.feign;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UniversityResponseDTO {

    private UUID id;

    private String name;

    private String universityImage;

    private String city;

    private String country;

    private List<String> fieldOfStudy;
    
    private List<CourseResponseDTO> courses;

    private Double tuitionFee;

    private String currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
