package com.hcl.springecomapp.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String resourceName;
	String field;
	String fieldName;
	Long fieldId;

	public UserNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s : %s", resourceName, field, fieldName));

		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public UserNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s not found with %s : %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}

	public UserNotFoundException(String resourceName, Long fieldId) {
		super(String.format("%s not found with ID : %d ", resourceName, fieldId));
		this.resourceName = resourceName;
		this.field = field;

	}
}
