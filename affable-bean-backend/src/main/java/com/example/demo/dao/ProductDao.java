package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Product;

public interface ProductDao extends JpaRepository<Product, Long>{

	@Query("""
			select p from Product p where p.category.name=:name
			""")
	List<Product> listProductByCategoryName(@Param("name")String categoryName);
	
}
