package com.example.demo.controller;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
public class HelloController {
	FilterChainProxy ex;
	// http://localhost:8080/api/jwt/hello
	@GetMapping("/hello")
	public String hello() {
		return "Hello Spring!";
	}

}
