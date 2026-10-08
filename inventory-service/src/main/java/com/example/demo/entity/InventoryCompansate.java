package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class InventoryCompansate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String orderId;
	@CollectionTable(name = "product_compansate")
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Product> products = new ArrayList<>();
	
	private String status;
	
	public void addProduct(Product product) {
		products.add(product);
	}

}
