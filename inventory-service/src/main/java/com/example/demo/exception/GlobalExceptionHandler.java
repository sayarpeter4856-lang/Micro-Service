package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler({ProductNotFoundException.class,
		OutOfStockException.class})
	public ResponseEntity<Object> globalExceptionHandler(Exception e,
			WebRequest request)throws Exception{
		return handleExceptionInternal(e,
				message(e),
				new HttpHeaders(), 
				HttpStatus.BAD_REQUEST,
				request);
	}
	
	private ApiError message(Exception e) {
		return new ApiError(HttpStatus.BAD_REQUEST.value(),
				Objects.nonNull(e) ? e.getMessage() :"Unknown Error",
				LocalDateTime.now());
	}

}
