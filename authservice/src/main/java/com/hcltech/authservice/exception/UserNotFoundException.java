package com.hcltech.authservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String resourceName;
	private final String field;
	private final String fieldName;

	public UserNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s : %s", resourceName, field, fieldName));

		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}
}
