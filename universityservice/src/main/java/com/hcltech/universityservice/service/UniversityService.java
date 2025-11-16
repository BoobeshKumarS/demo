package com.hcltech.universityservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hcltech.universityservice.dto.UniversityRequestDTO;
import com.hcltech.universityservice.dto.UniversityResponseDTO;

public interface UniversityService {
	UniversityResponseDTO create(UniversityRequestDTO universityRequestDTO, Authentication authentication);

	List<UniversityResponseDTO> getAll();

	UniversityResponseDTO getById(UUID id);
	
	UniversityResponseDTO getByAdminId(UUID adminId);

	void delete(UUID id);

	Page<UniversityResponseDTO> getAll(Pageable pageable);

	UniversityResponseDTO update(UUID id, UniversityRequestDTO universityRequestDTO);

}