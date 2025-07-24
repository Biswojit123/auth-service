package com.biswojit.autho.autho.exchandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<String> emailIdExistException(EmailAlreadyExistException emailIdException){
		
		return new ResponseEntity<String>(emailIdException.getMessage(),HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handelGlobalException(Exception e) {
		return new ResponseEntity<String>("Internal Server Error"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
