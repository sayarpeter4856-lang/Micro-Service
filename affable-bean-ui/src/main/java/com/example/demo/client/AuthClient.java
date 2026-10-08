package com.example.demo.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url="http://DISTRIBUTED-SECURITY-DEMO/api/auth")
public interface AuthClient {
	@PostExchange("/login")
	ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto);
}
