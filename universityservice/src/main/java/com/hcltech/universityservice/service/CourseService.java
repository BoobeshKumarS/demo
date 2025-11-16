package com.hcltech.universityservice.service;

import com.hcltech.universityservice.dto.CourseRequestDTO;
import com.hcltech.universityservice.dto.CourseResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponseDTO create(UUID universityId, CourseRequestDTO courseRequestDTO);
    CourseResponseDTO getCourseById(UUID id);
    List<CourseResponseDTO> getCoursesByUniversity(UUID universityId);
    List<CourseResponseDTO> getAllCourses(int page, int limit);
    CourseResponseDTO update(UUID id, CourseRequestDTO updatedCourse);
    void delete(UUID id);
    Long countCourses();
}