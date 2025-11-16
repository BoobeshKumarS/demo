package com.hcltech.applicationservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hcltech.applicationservice.dto.ApplicationFormRequestDTO;
import com.hcltech.applicationservice.dto.ApplicationFormResponseDTO;
import com.hcltech.applicationservice.entity.ApplicationForm;
import com.hcltech.applicationservice.entity.ApplicationStatus;
import com.hcltech.applicationservice.entity.PersonalInfo;
import com.hcltech.applicationservice.feign.UniversityClient;
import com.hcltech.applicationservice.feign.UniversityResponseDTO;
import com.hcltech.applicationservice.repository.ApplicationFormRepository;
import com.hcltech.applicationservice.service.ApplicationFormService;
import com.hcltech.applicationservice.util.ApplicationFormConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationFormServiceImpl implements ApplicationFormService {

	private final ApplicationFormRepository applicationFormRepository;

	private final ApplicationFormConverter applicationFormConverter;

	private final UniversityClient universityClient;

	@Override
	public ApplicationFormResponseDTO createApplication(ApplicationFormRequestDTO dto, UUID studentId) {

		ApplicationForm application = applicationFormConverter.toEntity(dto, studentId);

		UniversityResponseDTO universityResponse = universityClient.getById(application.getUniversityId());
		application.setUniversityName(universityResponse.getName());

		universityResponse.getCourses().stream().filter(course -> course.getId().equals(application.getCourseId()))
				.findFirst().ifPresent(course -> application.setCourseName(course.getName()));

		ApplicationForm saved = applicationFormRepository.save(application);

		ApplicationFormResponseDTO responseDTO = applicationFormConverter.convertToDTO(saved);

		return responseDTO;

	}

	// Automatically updates the status to UNDER_REVIEW
	// Then returns the application data
	@Override
	public ApplicationFormResponseDTO getApplicationById(UUID id) {
		ApplicationForm application = applicationFormRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

		// 🚀 Automatically move to UNDER_REVIEW if status is SUBMITTED
		if (application.getStatus() == ApplicationStatus.SUBMITTED) {
			application.setStatus(ApplicationStatus.UNDER_REVIEW);
			application.setUpdatedAt(LocalDateTime.now());
			applicationFormRepository.save(application);
		}

		return applicationFormConverter.convertToDTO(application);
	}

	@Override
	public List<ApplicationFormResponseDTO> getAllApplications() {
		return applicationFormRepository.findAll().stream().map(applicationFormConverter::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public ApplicationFormResponseDTO updateApplication(UUID id, ApplicationFormRequestDTO dto) {
		ApplicationForm existing = applicationFormRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

		// Update Personal Info
		if (dto.getPersonalInfo() != null) {
			PersonalInfo updated = applicationFormConverter.convertToEntity(dto.getPersonalInfo());
			updated.setId(existing.getPersonalInfo().getId()); // Preserve ID
			existing.setPersonalInfo(updated);
		}

		existing.setUpdatedAt(LocalDateTime.now());

		ApplicationForm saved = applicationFormRepository.save(existing);
		return applicationFormConverter.convertToDTO(saved);
	}

	@Override
	public void deleteApplicationById(UUID id) {
		ApplicationForm application = applicationFormRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

		applicationFormRepository.delete(application);
	}

	@Override
	public void submitApplication(UUID id) {
		ApplicationForm application = applicationFormRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

		if (application.getStatus() != ApplicationStatus.DRAFT) {
			throw new IllegalStateException("Only applications in DRAFT status can be submitted.");
		}

		application.setStatus(ApplicationStatus.SUBMITTED);
		application.setSubmittedAt(LocalDateTime.now());
		applicationFormRepository.save(application);
	}

	@Override
	public List<ApplicationFormResponseDTO> getApplicationsByStatus(ApplicationStatus status) {
		return applicationFormRepository.findByStatus(status).stream().map(applicationFormConverter::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<ApplicationFormResponseDTO> getRecentSubmittedApplications(int limit) {
		return applicationFormRepository.findByStatusOrderBySubmittedAtDesc(ApplicationStatus.SUBMITTED).stream()
				.limit(limit).map(applicationFormConverter::convertToDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ApplicationFormResponseDTO updateApplicationStatus(UUID id, ApplicationStatus status) {
		ApplicationForm app = applicationFormRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

		app.setStatus(status);
		app.setUpdatedAt(LocalDateTime.now());

		return applicationFormConverter.convertToDTO(applicationFormRepository.save(app));
	}

	@Override
	public List<ApplicationFormResponseDTO> getApplicationsByStudentId(UUID studentId) {
		return applicationFormRepository.findByStudentId(studentId).stream().map(applicationFormConverter::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<ApplicationFormResponseDTO> getApplicationsByUniversityId(UUID universityId) {
		return applicationFormRepository.findByUniversityId(universityId).stream()
				.map(applicationFormConverter::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Page<ApplicationFormResponseDTO> getPagedApplicationsByUniversityId(UUID universityId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return applicationFormRepository.findByUniversityId(universityId, pageable)
				.map(applicationFormConverter::convertToDTO);
	}

	@Override
	public boolean validateApplication(UUID applicationId) {
		return applicationFormRepository.existsById(applicationId);
	}

	@Override
	public boolean isApplicationMappedToStudent(UUID applicationId, UUID studentId) {
		return applicationFormRepository.existsByIdAndStudentId(applicationId, studentId);
	}
}