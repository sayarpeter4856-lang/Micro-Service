package com.example.demo.exception;

import java.time.LocalDateTime;

public record ApiError(
		int errorCode,
		String errorMessage,
		LocalDateTime occurAt) {

}
