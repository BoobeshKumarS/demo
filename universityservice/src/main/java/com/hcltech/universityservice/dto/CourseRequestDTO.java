package com.hcltech.universityservice.dto;

import com.hcltech.universityservice.entity.LevelOfEducation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRequestDTO {

	@NotBlank(message = "Course name is required")
	@Size(max = 100)
	private String name;

	@NotBlank(message = "Description is required")
	@Size(max = 500)
	private String description;

	@NotNull(message = "Level of education is required")
	private LevelOfEducation level;

	@Positive(message = "Duration must be greater than 0")
	@Max(value = 10, message = "Duration cannot exceed 10 years")
	private int duration;

	@NotBlank(message = "Language is required")
	private String language;

	@Positive(message = "Price must be greater than 0")
	private double price;

	@NotBlank(message = "Currency is required")
	@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO code (e.g., USD, INR)")
	private String currency;
}