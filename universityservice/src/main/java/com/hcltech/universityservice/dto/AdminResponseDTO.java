package com.hcltech.universityservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AdminResponseDTO {
	private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String nationality;
    private String address;
    private UniversityResponseDTO university;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
