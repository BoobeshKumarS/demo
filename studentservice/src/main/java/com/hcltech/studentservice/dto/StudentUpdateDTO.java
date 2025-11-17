package com.hcltech.studentservice.dto;

import java.time.LocalDate;

import com.hcltech.studentservice.entity.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for student update request.
 *
 * This DTO encapsulates all the necessary information required to register
 * or update a student in the system. It includes comprehensive validation
 * constraints to ensure data integrity and business rule compliance.
 *
 * All validation constraints are applied using Jakarta Bean Validation annotations
 * to provide automatic input validation at the controller layer.
 *
 * @author HCL Technologies
 * @version 1.0
 * @since 1.0
 */
@Data
public class StudentUpdateDTO {

    /**
     * The student's first name.
     * Required field with maximum length of 50 characters.
     */
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    /**
     * The student's last name.
     * Required field with maximum length of 50 characters.
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    /**
     * The student's date of birth.
     * Required field that must be a past date.
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * The student's phone number.
     * Optional field with E.164 international format validation.
     * Pattern accepts optional '+' followed by country code and number (1-15 digits).
     */
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
	private String phoneNumber;

    /**
     * The student's gender.
     * Required field from predefined Gender enumeration.
     */
    @NotNull(message = "Gender is required")
    private Gender gender;

    /**
     * The student's nationality.
     * Optional field with maximum length of 50 characters.
     */
    @Size(max = 50)
	private String nationality;

    /**
     * The student's residential address.
     * Optional field with no length restrictions.
     */
    private String address;
}