package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InSufficientAmountException extends ResponseStatusException{
	
	public InSufficientAmountException() {
		super(HttpStatus.BAD_REQUEST,"Amount is not enough!");
	}

}
