package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InventoryCompansate;


public interface InventoryCompansateDao extends JpaRepository<InventoryCompansate, Long>{
	Optional<InventoryCompansate> findByOrderId(String orderId);
	boolean existsByOrderId(String orderId);
}
