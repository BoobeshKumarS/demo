package com.hcltech.universityservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.universityservice.dto.AdminRequestDTO;
import com.hcltech.universityservice.dto.AdminResponseDTO;
import com.hcltech.universityservice.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admins")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/register")
	public ResponseEntity<AdminResponseDTO> registerAdmin(@Valid @RequestBody AdminRequestDTO adminRequest) {
		AdminResponseDTO adminResponse = adminService.registerAdmin(adminRequest);
		return new ResponseEntity<AdminResponseDTO>(adminResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable UUID id) {
		AdminResponseDTO adminResponse = adminService.getAdminById(id);
		return new ResponseEntity<AdminResponseDTO>(adminResponse, HttpStatus.OK);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
		List<AdminResponseDTO> admins = adminService.getAllAdmins();
		return new ResponseEntity<List<AdminResponseDTO>>(admins, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable UUID id,
			@Valid @RequestBody AdminRequestDTO adminRequest) {
		AdminResponseDTO adminResponse = adminService.updateAdmin(id, adminRequest);
		return new ResponseEntity<AdminResponseDTO>(adminResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
		adminService.deleteAdmin(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<AdminResponseDTO> getCurrentStudent(Authentication authentication) {
        return ResponseEntity.ok(adminService.getCurrentAdmin(authentication));
    }
}
