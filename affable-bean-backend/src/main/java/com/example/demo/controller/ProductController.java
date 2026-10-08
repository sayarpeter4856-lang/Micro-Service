package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backend")
public class ProductController {
	private final ProductService productService;
	
	
	record ProductDto(
			 Long id,
			 String name,
			 double price,
			 String description,
			 LocalDateTime lastUpdate
			) {}
	
	private ProductDto toDto(Product product) {
		return new ProductDto(product.getId()
				, product.getName()
				, product.getPrice()
				, product.getDescription()
				, product.getLastUpdate());
	}
	
	@GetMapping("/products/product/{id}")
	public ProductDto getProductById(@PathVariable("id") long id) {
		return toDto(productService.getProductById(id));
	}

	
	@GetMapping("/products/{name}")
	public List<ProductDto> listAllProducts(@PathVariable("name")String name){
		return productService.listProductByCategoryName(name)
				.stream()
				.map(this ::toDto)
				.toList();
	}
	
}
