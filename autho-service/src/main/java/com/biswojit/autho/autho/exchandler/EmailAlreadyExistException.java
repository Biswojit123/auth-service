package com.biswojit.autho.autho.exchandler;

@SuppressWarnings("serial")
public class EmailAlreadyExistException extends RuntimeException {
	
	public EmailAlreadyExistException(String message) {
		super(message);
	}
	
}
