package com.example.demo.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItem(
		String name,
		int quantity,
		double price
		) {

}
