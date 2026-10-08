package com.example.demo.cart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("Cart")
public class Cart {
	@Id
	private String id;
	private Set<CartItem> items=new HashSet<>();
	
	public void addItem(CartItem item) {
		items.add(item);
	}
	
	
	public void removeItem(long productId) {
		items.removeIf(item -> item.getId() == productId);
	}
	

}




