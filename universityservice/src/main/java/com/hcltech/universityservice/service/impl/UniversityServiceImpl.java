package com.hcltech.universityservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hcltech.universityservice.dto.UniversityRequestDTO;
import com.hcltech.universityservice.dto.UniversityResponseDTO;
import com.hcltech.universityservice.entity.Admin;
import com.hcltech.universityservice.entity.University;
import com.hcltech.universityservice.exception.ResourceAlreadyExistsException;
import com.hcltech.universityservice.exception.ResourceNotFoundException;
import com.hcltech.universityservice.repository.AdminRepository;
import com.hcltech.universityservice.repository.UniversityRepository;
import com.hcltech.universityservice.service.UniversityService;
import com.hcltech.universityservice.util.UniversityConverter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UniversityConverter universityConverter;

    @Override
    public Page<UniversityResponseDTO> getAll(Pageable pageable) {
        return universityRepository.findAll(pageable).map(universityConverter::toResponseDTO);
    }

    @Override
    @Transactional
    public UniversityResponseDTO create(UniversityRequestDTO universityRequestDTO, Authentication authentication) {
        String email = authentication.getName();
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found", "email", email));

        // Check if admin already has a university
        if (admin.getUniversity() != null) {
            throw new ResourceAlreadyExistsException("Admin already has a university", "university ID", admin.getUniversity().getId());
        }

        University university = universityConverter.toEntity(universityRequestDTO);
        // Set both sides of the relationship
        university.setAdmin(admin);
        admin.setUniversity(university);
        University saved = universityRepository.save(university);
        return universityConverter.toResponseDTO(saved);
    }

    public List<UniversityResponseDTO> getAll() {
        return universityRepository.findAll().stream().map(universityConverter::toResponseDTO).toList();
    }

    public UniversityResponseDTO getById(UUID adminId) {
    	 University university = universityRepository.findById(adminId)
                 .orElseThrow(() -> new ResourceNotFoundException("University not found", "admin ID", adminId));
    	 return universityConverter.toResponseDTO(university);
    }
    
    public UniversityResponseDTO getByAdminId(UUID id) {
   	 University university = universityRepository.findByAdminId(id)
                .orElseThrow(() -> new ResourceNotFoundException("University not found", "university ID", id));
   	 return universityConverter.toResponseDTO(university);
   }

    @Transactional
    public void delete(UUID universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University not found", "university ID", universityId));

        // Courses are deleted automatically due to CascadeType.ALL in courses

        // Detach admin so it is not deleted
        if (university.getAdmin() != null) {
            university.getAdmin().setUniversity(null);
        }

        // Delete university
        universityRepository.delete(university);
    }

    @Override
    public UniversityResponseDTO update(UUID id, UniversityRequestDTO dto) {
        University existingUniversity = universityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("University not found"));

        // Update only the provided fields
        if (dto.getName() != null) {
            existingUniversity.setName(dto.getName());
        }
        if (dto.getCity() != null) {
            existingUniversity.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            existingUniversity.setState(dto.getState());
        }
        if (dto.getCountry() != null) {
            existingUniversity.setCountry(dto.getCountry());
        }

        return universityConverter.toResponseDTO(universityRepository.save(existingUniversity));
    }

//    private University findUniversityById(UUID id) {
//        return universityRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("University not found", "university ID", id));
//    }

}
