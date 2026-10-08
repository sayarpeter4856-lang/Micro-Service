package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.OrderRabbitConfig;
import com.example.demo.dao.OrderDao;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;



@Service
public class OrderRabbitListenerService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public record PaymentSuccessRequest(String orderCode) {}
	public record inventoryCompansationDto(String orderId, String message) {}
	
	@RabbitListener(queues = "inventory.compansation.queue")
	public void paymentFailListener(inventoryCompansationDto iDto) {
		Order order = changeOrderStatusOrder(iDto.orderId, OrderStatus.CANCEL);
		var orderDto = new OrderCancelDto(order.getOrderCode(), iDto.message);
		rabbitTemplate.convertAndSend(OrderRabbitConfig.EXCHANGE, 
				OrderRabbitConfig.ORDER_CANCEL_FROM_INVENTORY_FAIL_BINDINGKEY,
				orderDto);
		
	}
	public record OrderCancelDto(String orderCode, String Message) {}
	@RabbitListener(queues = "payment_success_queue")
	public void paymentSuccessListener(PaymentSuccessRequest request) {
		Order order = changeOrderStatusOrder(request.orderCode, OrderStatus.SUCCESS);
		var orderSuccess=new PaymentSuccessRequest(order.getOrderCode());
		rabbitTemplate
		.convertAndSend(OrderRabbitConfig.EXCHANGE,"order.success",
				orderSuccess);
	}

	private Order changeOrderStatusOrder(String orderCode, OrderStatus status) {
		Order order=orderDao.findByOrderCode(orderCode)
				.get();
		order.setStatus(status);
		orderDao.save(order);
		return order;
	}
	
	public record OrderFailRequest(String orderId, String message) {}
	public record InventoryFailRequest(String orderId, String message) {}
	
	@RabbitListener(queues = "inventory.fail.queue")
	public void inventoryFailListener(InventoryFailRequest request) {
		Order order=changeOrderStatusOrder(request.orderId, OrderStatus.CANCEL);
		var orderFailRequest = new OrderFailRequest(request.orderId, request.message);
		rabbitTemplate.convertAndSend(OrderRabbitConfig.EXCHANGE, 
				"order.fail", orderFailRequest);
	}
	

}






