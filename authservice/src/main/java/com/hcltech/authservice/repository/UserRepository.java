package com.hcltech.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.authservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String identifier);
	Optional<User> findByUsername(String identifier);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	User findUserByEmail(String email);
}
