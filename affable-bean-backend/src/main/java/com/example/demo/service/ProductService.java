package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;
import com.example.demo.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductDao productDao;
	
	public Product getProductById(long id) {
		return productDao.findById(id)
				.orElseThrow();
	}
	
	public List<Product> listProductByCategoryName(String categoryName){
		return productDao.listProductByCategoryName(categoryName);
	}
	
}
