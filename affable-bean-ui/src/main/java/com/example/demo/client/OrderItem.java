package com.example.demo.client;


public record OrderItem(
		String name,
		int quantity,
		double price
		) {

}
