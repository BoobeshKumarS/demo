package com.hcltech.universityservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hcltech.universityservice.dto.CourseRequestDTO;
import com.hcltech.universityservice.dto.CourseResponseDTO;
import com.hcltech.universityservice.entity.Course;
import com.hcltech.universityservice.entity.University;
import com.hcltech.universityservice.exception.ResourceNotFoundException;
import com.hcltech.universityservice.repository.CourseRepository;
import com.hcltech.universityservice.repository.UniversityRepository;
import com.hcltech.universityservice.service.CourseService;
import com.hcltech.universityservice.util.CourseConverter;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UniversityRepository universityRepo;

	@Autowired
	private CourseConverter courseConverter;

	@Override
	public CourseResponseDTO create(UUID universityId, CourseRequestDTO courseRequestDTO) {
		University university = universityRepo.findById(universityId).orElseThrow(
				() -> new ResourceNotFoundException("University not found", "university ID", universityId));

		Course course = courseConverter.toEntity(courseRequestDTO);
		course.setUniversity(university);
		Course savedCourse = courseRepository.save(course);
		return courseConverter.toResponseDTO(savedCourse);
	}

	@Override
	public List<CourseResponseDTO> getCoursesByUniversity(UUID universityId) {
		University university = universityRepo.findById(universityId).orElseThrow(
				() -> new ResourceNotFoundException("University not found", "university ID", universityId));
		return university.getCourses().stream().map(courseConverter::toResponseDTO).toList();
	}

	@Override
	public List<CourseResponseDTO> getAllCourses(int page, int limit) {
		Pageable pageable = PageRequest.of(page - 1, limit);
		Page<Course> coursePage = courseRepository.findAll(pageable);
		return coursePage.getContent().stream().map(courseConverter::toResponseDTO).toList();
	}

	@Override
	public CourseResponseDTO getCourseById(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found", "course ID", id));
		return courseConverter.toResponseDTO(course);
	}

	@Override
	public CourseResponseDTO update(UUID id, CourseRequestDTO updatedCourse) {
		Course existing = courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found", "course ID", id));

		existing.setName(updatedCourse.getName());
		existing.setDescription(updatedCourse.getDescription());
		existing.setLevel(updatedCourse.getLevel());
		existing.setDuration(updatedCourse.getDuration());
		existing.setLanguage(updatedCourse.getLanguage());
		existing.setPrice(updatedCourse.getPrice());
		existing.setCurrency(updatedCourse.getCurrency());

		// No fieldOfStudy update here since it's tied to University

		return courseConverter.toResponseDTO(courseRepository.save(existing));
	}

	@Override
	public void delete(UUID id) {
		if (!courseRepository.existsById(id)) {
			throw new ResourceNotFoundException("Course not found", "course ID", id);
		}
		courseRepository.deleteById(id);
	}
	
	@Override
    public Long countCourses() {
        return courseRepository.count();
    }
}