package com.example.demo.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dao.OrderDao;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;

import lombok.RequiredArgsConstructor;
/*
 * http:// ? synchronous
 * send and listen - jms://  .backend
 */
@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderDao orderDao;
	private final RabbitTemplate rabbitTemplate;
	
	public record InventoryRequest(
			long productId,
			int debutQuantity,
			String username) {
		
	}
	public record InventoryRequestBody(
			String username,
			String accountNumber,
			double totalAmount,
			String orderCode,
			Map<String,Integer> products) {
		
	}
	
	public String saveOrder(String username,List<OrderItem> orderItems,
			String accountNumber,String orderId) {
		
		Order order=new Order(orderId,
				OrderStatus.PENDING, username);
		orderItems.stream().forEach(order::addOrderItem);
		orderDao.save(order);
		
		
		Map<String,Integer> inventoryProducts=new HashMap<>();
		double total=0;
		for(OrderItem item:orderItems) {
			inventoryProducts.put(item.name(),item.quantity());
			total +=item.price();
		}
		var requestBody=new InventoryRequestBody(
				username,
				accountNumber,
				total,
				order.getOrderCode(),
				inventoryProducts);
		rabbitTemplate.convertAndSend("saga.exchange",
				"order.created",requestBody);
		return "successful created new order!";
	}
	

}
