package com.example.demo.dto;

import java.time.LocalDateTime;

public record ProductDto(
		long id,
		String name,
		String description, 
		LocalDateTime lastUpdate,
		double price,
		String categoryName) {
}
