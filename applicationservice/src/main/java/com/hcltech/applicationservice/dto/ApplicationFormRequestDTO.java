package com.hcltech.applicationservice.dto;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationFormRequestDTO {

    private UUID universityId;
    private UUID courseId;


    @NotNull(message = "Personal information is required")
    @Valid
    private PersonalInfoDTO personalInfo;

    @AssertTrue(message = "You must agree to the terms before submitting")
    private boolean agreedToTerms;
}
