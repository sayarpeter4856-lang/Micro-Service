package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountNoFoundException extends ResponseStatusException{
	public AccountNoFoundException() {
		super(HttpStatus.BAD_REQUEST,"Account Not Found!");
	}

}
