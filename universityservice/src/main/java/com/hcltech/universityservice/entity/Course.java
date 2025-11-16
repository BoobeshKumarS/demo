package com.hcltech.universityservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank(message = "Course name is required")
	@Size(max = 100)
	private String name;

	@NotBlank(message = "Description is required")
	@Size(max = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Level of education is required")
	private LevelOfEducation level;

	@Positive(message = "Duration must be greater than 0")
	@Max(value = 10, message = "Duration cannot exceed 10 years")
	private int duration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "university_id", nullable = false)
	@NotNull(message = "University is required")
	private University university;

	@NotBlank(message = "Language is required")
	private String language;

	@Positive(message = "Price must be greater than 0")
	private double price;

	@NotBlank(message = "Currency is required")
	@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO code (e.g., USD, INR)")
	private String currency;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}