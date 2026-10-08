package com.example.demo.order.success;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface OrderInfoReposity extends JpaRepository<OrderInfo, Long> {
	
	Optional<OrderInfo> findByOrderId(String orderId);

}
