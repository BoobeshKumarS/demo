package com.hcltech.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationValidationResponseDTO {
    private boolean valid;
    private String message;
    private UUID studentId;
}