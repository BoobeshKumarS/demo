package com.hcltech.universityservice.util;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcltech.universityservice.dto.AdminRequestDTO;
import com.hcltech.universityservice.dto.AdminResponseDTO;
import com.hcltech.universityservice.entity.Admin;

@Component
public class AdminConverter {
	@Autowired
	@Lazy
	private UniversityConverter universityConverter;
	
	public AdminResponseDTO toResponse(Admin admin) {
		AdminResponseDTO.AdminResponseDTOBuilder builder =  AdminResponseDTO.builder()
				.id(admin.getId())
				.firstName(admin.getFirstName())
				.lastName(admin.getLastName())
				.email(admin.getEmail())
				.dateOfBirth(admin.getDateOfBirth())
				.gender(admin.getGender().getDisplayName())
				.phoneNumber(admin.getPhoneNumber())
				.nationality(admin.getNationality())
				.university(null)
				.createdAt(admin.getCreatedAt())
				.updatedAt(admin.getUpdatedAt());
		return builder.build();
	}
	
	public Admin toEntity(AdminRequestDTO adminRequest) {
		Admin.AdminBuilder admin = Admin.builder()
				.firstName(adminRequest.getFirstName())
				.lastName(adminRequest.getLastName())
				.email(adminRequest.getEmail())
				.dateOfBirth(adminRequest.getDateOfBirth())
				.phoneNumber(adminRequest.getPhoneNumber())
				.gender(adminRequest.getGender())
				.nationality(adminRequest.getNationality());
		
		return admin.build();
	}
}
