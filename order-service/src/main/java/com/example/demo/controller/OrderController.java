package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.OrderItem;
import com.example.demo.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	
	record OrderRequest(String username,List<OrderItem> orderItems,
			String accountNumber,String orderId) {}
	// http://localhost:8080/api/order/new-order
	@PostMapping("/new-order")
	public ResponseEntity<String> 
		createOrder(
				@RequestHeader("Authorization")String token,
				@RequestBody OrderRequest request){
		System.out.println("OrderServices:token:"+ token);
		String returnString=orderService.saveOrder(request.username,
				request.orderItems,request.accountNumber,request.orderId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(returnString);
	}
	

}
