package com.hcltech.universityservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UniversityRequestDTO {

    @NotBlank(message = "University name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String State;

    @NotBlank(message = "Country is required")
    private String country;
}