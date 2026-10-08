package com.example.demo.order.success;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String orderId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	private String message;
	
	public OrderInfo(String orderId, OrderStatus orderStatus) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
	}
	
	
	

}
