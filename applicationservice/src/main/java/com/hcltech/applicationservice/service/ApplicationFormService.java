package com.hcltech.applicationservice.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import com.hcltech.applicationservice.dto.ApplicationFormRequestDTO;
import com.hcltech.applicationservice.dto.ApplicationFormResponseDTO;
import com.hcltech.applicationservice.entity.ApplicationStatus;

import java.util.List;
import java.util.UUID;

public interface ApplicationFormService {
    ApplicationFormResponseDTO createApplication(ApplicationFormRequestDTO dto, UUID studentId);
    ApplicationFormResponseDTO getApplicationById(UUID id);
    List<ApplicationFormResponseDTO> getAllApplications();
    void submitApplication(UUID id);
    void deleteApplicationById(UUID id);
    ApplicationFormResponseDTO updateApplication(UUID id, @Valid ApplicationFormRequestDTO dto);
    
    List<ApplicationFormResponseDTO> getApplicationsByStatus(ApplicationStatus status);
    List<ApplicationFormResponseDTO> getRecentSubmittedApplications(int limit);
    ApplicationFormResponseDTO updateApplicationStatus(UUID id, ApplicationStatus status);

    List<ApplicationFormResponseDTO> getApplicationsByStudentId(UUID studentId);
    List<ApplicationFormResponseDTO> getApplicationsByUniversityId(UUID universityId);
    Page<ApplicationFormResponseDTO> getPagedApplicationsByUniversityId(UUID universityId, int page, int size);

    boolean validateApplication(UUID studentId);
    boolean isApplicationMappedToStudent(UUID applicationId, UUID studentId);
}