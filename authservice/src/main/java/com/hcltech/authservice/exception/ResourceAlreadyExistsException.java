package com.hcltech.authservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String resourceName;
    private final String field;
    private final String fieldName;
    private final Long fieldId;

    public ResourceAlreadyExistsException(String resourceName, String field, String fieldName) {
        super(String.format("%s already exists with %s : %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
        this.fieldId = null;
    }

    public ResourceAlreadyExistsException(String resourceName, String field, Long fieldId) {
        super(String.format("%s already exists with %s : %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
        this.fieldName = null;
    }

    public ResourceAlreadyExistsException(String resourceName, String field) {
        super(String.format("%s already exists %s", resourceName, field));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = null;
        this.fieldId = null;
    }
}
