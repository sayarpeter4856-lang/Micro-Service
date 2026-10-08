package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
	public static final String EXCHANGE ="saga.exchange";
	public static final String INVENTORY_FAIL_QUEUE ="inventory.fail.queue";
	public static final String PAYMENT_QUEUE ="payment.processing.queue";
	public static final String ROUTING_KEY_INVENTORY_SUCCESS="inventory.stock.decreased";
	public static final String INVENTORY_COMPANSATION_QUEUE="inventory.compansation.queue";
	public static final String INVENTORY_COMPANSATION_BININGE_KEY="inventory.compansation";
	@Bean 
	public Queue inventoryCompansatoinQueue() {
		return new Queue(INVENTORY_COMPANSATION_QUEUE);
	}
	@Bean
	public Binding inventoryCompansationBinding(@Qualifier("inventoryCompansatoinQueue") 
	Queue inventoryCompansatoinQueue, TopicExchange exchanage) {
		return BindingBuilder.bind(inventoryCompansatoinQueue)
				.to(exchanage)
				.with(INVENTORY_COMPANSATION_BININGE_KEY);
	}
	@Bean
	public TopicExchange sagaExchange() {
		return new TopicExchange(EXCHANGE);
	}
	@Bean
	public Queue inventoryFailQueue() {
		return new Queue(INVENTORY_FAIL_QUEUE);
	}
	@Bean
	public Binding inventoryFailBinding(@Qualifier("inventoryFailQueue")
	Queue inventoryFailQueue,TopicExchange exchanage) {
		return BindingBuilder.bind(inventoryFailQueue)
				.to(exchanage).with("inventory.outofstock");
	}
	@Bean
	public Queue paymentQueue() {
		return new Queue(PAYMENT_QUEUE);
	}
	@Bean
	public Binding bindingPayment(
			Queue paymentQueue,
			TopicExchange sagaExchange) {
		return BindingBuilder.bind(paymentQueue)
				.to(sagaExchange)
				.with(ROUTING_KEY_INVENTORY_SUCCESS);
	}
	
	
	
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	

}
