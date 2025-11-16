package com.hcltech.universityservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hcltech.universityservice.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
	List<Course> findByUniversityId(UUID universityId);
}
