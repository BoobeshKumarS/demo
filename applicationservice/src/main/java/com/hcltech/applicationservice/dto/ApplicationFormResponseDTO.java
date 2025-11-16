package com.hcltech.applicationservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.hcltech.applicationservice.entity.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationFormResponseDTO {
    private UUID applicationId;

    private UUID universityId;
    private String universityName;
    
    private UUID courseId;
    private String courseName;

    private UUID studentId;
    private PersonalInfoDTO personalInfo;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
}
