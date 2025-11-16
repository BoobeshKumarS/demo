package com.hcltech.studentservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.studentservice.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
	Optional<Student> findByEmail(String email);
}
