package com.example.demo.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record Product(long productId, String name, int quantity){
	
}
