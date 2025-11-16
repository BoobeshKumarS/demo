package com.hcltech.universityservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcltech.universityservice.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
	Optional<Admin> findByEmail(String email);
}
