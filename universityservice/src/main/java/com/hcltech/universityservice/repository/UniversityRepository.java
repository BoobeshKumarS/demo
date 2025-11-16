package com.hcltech.universityservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcltech.universityservice.entity.University;

public interface UniversityRepository extends JpaRepository<University, UUID> {

	Optional<University> findByAdminId(UUID adminId);

}
