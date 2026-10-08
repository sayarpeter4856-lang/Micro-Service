package com.example.demo.client;

import java.util.List;

public record OrderRequest(String username,List<OrderItem> orderItems,
		String accountNumber, String orderId) {}
