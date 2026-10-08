package com.example.demo.entity;

import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

	@Id
	private Long id;
	private String name;
	private double price;
	@Column(columnDefinition = "text")
	private String description;
	@Column(name = "last_update")
	private LocalDateTime lastUpdate;
	@ManyToOne
	private Category category;
	
}
