package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OutOfStockException extends ResponseStatusException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfStockException() {
		super(HttpStatus.BAD_REQUEST,"Item is Out of Stock!");
	}

}
