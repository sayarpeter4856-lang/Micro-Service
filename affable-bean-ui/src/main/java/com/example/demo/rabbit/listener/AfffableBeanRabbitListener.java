package com.example.demo.rabbit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.demo.order.success.OrderInfo;
import com.example.demo.order.success.OrderInfoReposity;
import com.example.demo.order.success.OrderStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AfffableBeanRabbitListener {
	private final OrderInfoReposity orderInfoReposity;
	
	public record PaymentSuccessRequest(String orderCode) {}
	public record OrderFailRequest(String orderId, String message) {}
	
	@RabbitListener(queues = "fail.order.queue")
	public void orderFailListener(OrderFailRequest request) {
		changeOrderStatus(OrderStatus.ORDER_FAIL, request.orderId, request.message);
	}
	
	@RabbitListener(queues="success.order.queue")
	public void orderSuccessListener(PaymentSuccessRequest request) {
		changeOrderStatus(OrderStatus.ORDER_SUCCESS, request.orderCode, "Order Successful!!");
	}
	
	public record OrderCancelDto(String orderCode, String Message) {}
	@RabbitListener(queues = "order.fail.from.inventory.queue")
	public void orderFailListenerPaymentFail(OrderCancelDto dto) {
		changeOrderStatus(OrderStatus.ORDER_FAIL, dto.orderCode, dto.Message);
	}
	
	// Refactoring method
	private void changeOrderStatus(OrderStatus orderStatus, String orderId, String message) {
		orderInfoReposity.findByOrderId(orderId)
		.ifPresentOrElse(order -> {
			System.out.println("Order already existed.");
		}, () -> {
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(orderId);
			orderInfo.setOrderStatus(orderStatus);
			orderInfo.setMessage(message);
			orderInfoReposity.save(orderInfo);
		});
	}
	
}
