package com.hcltech.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<APIResponse> myUserNotFoundException(UserNotFoundException ex){
		String message = ex.getMessage();
		APIResponse apiResponse = new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<APIResponse> myResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
		String message = ex.getMessage();
		APIResponse apiResponse = new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.CONFLICT);
		
	}
}
