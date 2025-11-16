package com.hcltech.universityservice.exception;

import java.util.UUID;

public class ResourceAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	String resourceName;
	String field;
	String fieldName;
	UUID fieldId;

	public ResourceAlreadyExistsException() {
		super();
	}

	public ResourceAlreadyExistsException(String resourceName, String field, String fieldName) {
		super(String.format("%s already exists with %s : %s", resourceName, field, fieldName));

		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResourceAlreadyExistsException(String resourceName, String field, UUID fieldId) {
		super(String.format("%s already exists with %s : %s", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}

	public ResourceAlreadyExistsException(String resourceName, String field) {
		super(String.format("%s already exists %s ", resourceName, field));
		this.resourceName = resourceName;
		this.field = field;

	}
}
