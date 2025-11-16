package com.hcltech.applicationservice.util;

import org.springframework.stereotype.Component;

import com.hcltech.applicationservice.dto.ApplicationFormRequestDTO;
import com.hcltech.applicationservice.dto.ApplicationFormResponseDTO;
import com.hcltech.applicationservice.dto.PersonalInfoDTO;
import com.hcltech.applicationservice.entity.ApplicationForm;
import com.hcltech.applicationservice.entity.ApplicationStatus;
import com.hcltech.applicationservice.entity.PersonalInfo;

import java.util.UUID;

@Component
public class ApplicationFormConverter {
	
    public PersonalInfo convertToEntity(PersonalInfoDTO dto) {
        return PersonalInfo.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .country(dto.getCountry())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public ApplicationFormResponseDTO convertToDTO(ApplicationForm entity) {
        return ApplicationFormResponseDTO.builder()
                .applicationId(entity.getId())
                .universityId(entity.getUniversityId())
                .universityName(entity.getUniversityName())
                .courseId(entity.getCourseId())
                .courseName(entity.getCourseName())
                .studentId(entity.getStudentId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .submittedAt(entity.getSubmittedAt())
                .personalInfo(convertToDTO(entity.getPersonalInfo()))
                .build();
    }

    private PersonalInfoDTO convertToDTO(PersonalInfo p) {
        return PersonalInfoDTO.builder()
                .fullName(p.getFullName())
                .email(p.getEmail())
                .country(p.getCountry())
                .phoneNumber(p.getPhoneNumber())
                .build();
    }

    public ApplicationForm toEntity(ApplicationFormRequestDTO dto, UUID studentId) {
        ApplicationForm form = ApplicationForm.builder()
                .personalInfo(convertToEntity(dto.getPersonalInfo()))
                .agreedToTerms(dto.isAgreedToTerms())
                .status(ApplicationStatus.DRAFT)
                .studentId(studentId)
                .build();

        form.setUniversityId(dto.getUniversityId());
        form.setCourseId(dto.getCourseId());

        return form;
    }
}