package com.example.demo.dto;

import com.example.demo.entity.InventoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductInventoryDto {
	private String username;
	private InventoryStatus inventoryStatus;
	private long productId;
	private String name;
	private int debutQuantity; 
	private int quantity;
	
	
	public ProductInventoryDto(String username, InventoryStatus inventoryStatus, long productId, String name,
			int debutQuantity, int quantity) {
		super();
		this.username = username;
		this.inventoryStatus = inventoryStatus;
		this.productId = productId;
		this.name = name;
		this.debutQuantity = debutQuantity;
		this.quantity = quantity;
	}
	
	
}
