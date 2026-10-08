package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Order;

public interface OrderDao extends JpaRepository<Order,Long>{
	 Optional<Order> findByOrderCode(String orderCode);

}
