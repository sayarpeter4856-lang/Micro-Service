package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String orderCode;
	@CreationTimestamp
	private LocalDateTime cratedAt;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	private String username;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "affable_bean_order_items")
	private List<OrderItem> orderItems=
			new ArrayList<>();
	
	
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}
	public Order(String orderCode, OrderStatus status, String username) {
		super();
		this.orderCode = orderCode;
		this.status = status;
		this.username = username;
	}

}




