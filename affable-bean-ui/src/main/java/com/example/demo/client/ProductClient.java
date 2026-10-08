package com.example.demo.client;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import com.example.demo.dto.ProductDto;

@HttpExchange(url = "http://AFFABLE-BEAN-BACKEND/api/backend")
public interface ProductClient {
	
	@GetExchange("/products/{name}")
	List<ProductDto> listAllProducts(@PathVariable("name")String name);
	
	@GetExchange("/products/product/{id}")
	ProductDto getProductById(@PathVariable("id")long id);
}
