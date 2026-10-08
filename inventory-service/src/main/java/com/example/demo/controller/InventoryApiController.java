package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ProductInventoryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/inventory")
@RestController
@RequiredArgsConstructor
public class InventoryApiController {
	private final ProductInventoryService productInventoryService;
	// debutQuentity(long productId,int debutQuentity,String username)
	public record InventoryRequest(
			long productId,
			int debutQuantity,
			String username) {
		
	}
	 // http://localhost:8080/api/inventory/debut
	@PostMapping("/debut")
	public ResponseEntity<String> debutQuantity(@RequestBody InventoryRequest request){
		String returnString=productInventoryService
				.debutQuentity(request.productId,
						request.debutQuantity,
						request.username);
		return new ResponseEntity(returnString,HttpStatus.OK);
	}
}








