package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;
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
	@ExceptionHandler({AccountNoFoundException.class,
		InSufficientAmountException.class})
	public ResponseEntity globalExceptionHandler(Exception ex,
			WebRequest request)
		throws Exception{
		Map body=Map.of("status",HttpStatus.BAD_REQUEST.value(),
				"error-message",Objects.nonNull(ex)?ex.getMessage():"Unknown Error!",
						"OccuredAt",LocalDateTime.now());
		return handleExceptionInternal(ex, body, new HttpHeaders(),
				HttpStatus.BAD_GATEWAY, request);
	}
}
