package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProductInventoryAudit;

public interface ProductInventoryAuditDao 
	extends JpaRepository<ProductInventoryAudit,Long>{

}
