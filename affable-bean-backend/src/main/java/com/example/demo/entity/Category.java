package com.example.demo.entity;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
	@Id
	private Long id;
	private String name;
	@OneToMany(mappedBy = "category")
	private List<Product> products =new ArrayList<>();
}
