package com.hcltech.applicationservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hcltech.applicationservice.entity.ApplicationForm;
import com.hcltech.applicationservice.entity.ApplicationStatus;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, UUID> {
	List<ApplicationForm> findByStatus(ApplicationStatus status);
	List<ApplicationForm> findByStatusOrderBySubmittedAtDesc(ApplicationStatus submitted);
	List<ApplicationForm> findByStudentId(UUID studentId);
	List<ApplicationForm> findByUniversityId(UUID universityId);
    Page<ApplicationForm> findByUniversityId(UUID universityId, Pageable pageable);
    
    boolean existsByIdAndStudentId(UUID applicationId, UUID studentId);
}
