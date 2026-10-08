package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.cart.CartItem;
import com.example.demo.cart.CartService;
import com.example.demo.client.AuthClient;
import com.example.demo.client.LoginDto;
import com.example.demo.client.LoginResponse;
import com.example.demo.client.OrderClient;
import com.example.demo.client.OrderItem;
import com.example.demo.client.OrderRequest;
import com.example.demo.client.ProductClient;
import com.example.demo.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AffableBeanService {
	
	private final ProductClient productClient;
	private final AuthClient authClient;
	private final OrderClient orderClient;
	private final CartService cartService;
	
	public List<ProductDto> listAllProducts(String name){
		return productClient.listAllProducts(name);
	}
	
	private OrderItem toOrderItem(CartItem cartItem) {
		return new OrderItem(cartItem.getName()
				,cartItem.getQuantity()
				,cartItem.getPrice());
	}
	
	public ProductDto getProductById(long id) {
		return productClient.getProductById(id);
	}
	
	public String checkout(String username,String password,String accountNumber, String orderId) {
		LoginDto login=new LoginDto(username, password);
		ResponseEntity<LoginResponse> response=authClient.login(login);
		if(response.getStatusCode().is2xxSuccessful()
				&& Objects.nonNull(response.getBody().token())) {
			 String jwtToken = "Bearer "+ response.getBody().token();
			 List<OrderItem> orderItems=cartService.getMyCart()
					 .getItems()
					 .stream()
					 .map(this::toOrderItem)
					 .toList();
			 OrderRequest request=new OrderRequest(username, orderItems,
					 accountNumber, orderId);
			 orderClient.createOrder(jwtToken, request);
		}
		else {
			System.out.println("Transport Error!");
		}
		
		return "checkout success";
	}
}
