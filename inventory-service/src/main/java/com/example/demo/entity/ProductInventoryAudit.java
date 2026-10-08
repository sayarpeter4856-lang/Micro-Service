package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductInventoryAudit {
	//who when what
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private int debutQuantity; //5
	@CreationTimestamp
	private LocalDateTime createAt;
	@Enumerated(EnumType.STRING)
	private InventoryStatus inventoryStatus; //debut
	@ManyToOne(cascade = {CascadeType.PERSIST})
	private ProductInventory productInventory;
	
	public ProductInventoryAudit(String username, int debutQuantity, InventoryStatus inventoryStatus) {
		super();
		this.username = username;
		this.debutQuantity = debutQuantity;
		this.inventoryStatus = inventoryStatus;
	}
	
	
}






