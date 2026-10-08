package com.example.demo.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "http://ORDER-SERVICE/api/order")
public interface OrderClient {
	@PostExchange("/new-order")
	void createOrder(
			@RequestHeader("Authorization")String token,
			@RequestBody OrderRequest request
			);
}
