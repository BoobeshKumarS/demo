package com.hcltech.universityservice.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String resourceName;
	String field;
	String fieldName;
	UUID fieldId;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s : %s", resourceName, field, fieldName));

		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public UserNotFoundException(String resourceName, String field, UUID fieldId) {
		super(String.format("%s not found with %s : %s", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}

	public UserNotFoundException(String resourceName, UUID fieldId) {
		super(String.format("%s not found with ID : %s ", resourceName, fieldId));
		this.resourceName = resourceName;
		this.fieldId = fieldId;
	}
}
