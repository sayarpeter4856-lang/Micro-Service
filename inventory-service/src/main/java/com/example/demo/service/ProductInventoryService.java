package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.RabbitMqConfig;
import com.example.demo.dao.InventoryCompansateDao;
import com.example.demo.dao.ProductInventoryAuditDao;
import com.example.demo.dao.ProductInventoryDao;
import com.example.demo.entity.InventoryCompansate;
import com.example.demo.entity.InventoryStatus;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductInventory;
import com.example.demo.entity.ProductInventoryAudit;
import com.example.demo.exception.OutOfStockException;
import com.example.demo.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ProductInventoryService {
	@Autowired
	private ProductInventoryAuditDao productInventoryAuditDao;
	@Autowired
	private ProductInventoryDao productInventoryDao;
	@Autowired
	private InventoryCompansateDao inventoryCompansateDao;

	public List<ProductInventory> listAllProductInventory() {
		return productInventoryDao.findAll();
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public record InventoryRequestBody(String username, String accountNumber, double totalAmount, String orderCode,
			Map<String, Integer> products) {}

	public record InventoryFailRequest(String orderId) {}

	@RabbitListener(queues = "order.queue")
	@Transactional
	public void handleRabbitMQInventoryRequestFromOrder(InventoryRequestBody request) {
		System.out.println("Inventory Debut Rabbit Listener::" + request);
		Map<String, Integer> products = request.products();
		String orderCode = request.orderCode;

		List<Product> productsList = new ArrayList<>();
		InventoryCompansate inventoryCompansate = new InventoryCompansate();
		inventoryCompansate.setOrderId(orderCode);

		for (String key : products.keySet()) {
			ProductInventory productInventory = productInventoryDao.findByName(key).orElseThrow();
			try {
				if (productInventory.getQuantity() < products.get(key)) {
					System.out.println("OutOfStock!"); // Rabbit -OrderCancel
					throw new RuntimeException();
				} else {
					productInventory.setQuantity(productInventory.getQuantity() - products.get(key));
					ProductInventoryAudit audit = new ProductInventoryAudit(request.username, products.get(key),
							InventoryStatus.DEBUT);
					audit.setProductInventory(productInventory);
					productInventoryAuditDao.save(audit);

					// for compansation-----
					Product product = new Product(productInventory.getProductId(), key, products.get(key));
					productsList.add(product);

					var paymentRequest = new PaymentRequest(request.username, request.accountNumber,
							request.totalAmount, request.orderCode);
					rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY_INVENTORY_SUCCESS,
							paymentRequest);
				}

			} catch (Exception e) {
				var ob = new InventoryFailRequest(orderCode);
				rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, "inventory.outofstock", ob);
			}
			System.out.println("%s items: %s.".formatted(key, products.get(key)));
		}
		inventoryCompansate.setProducts(productsList);
		inventoryCompansate.setStatus("Pending");
		if(!inventoryCompansateDao.existsByOrderId(request.orderCode)) {
			inventoryCompansateDao.save(inventoryCompansate);
		}
	}

	public record PaymentRequest(String fromUser, String fromAccountNumber, double amount, String orderCode) {
	}

	// debutQuentity(long productId,int debutQuentity,String username)
	public String debutQuentity(long productId, int debutQuentity, String username) {
		ProductInventory beforeDebutProduct = findProductInventoryById(productId);
		if (beforeDebutProduct.getQuantity() < debutQuentity) {
			throw new OutOfStockException();
		}
		beforeDebutProduct.setQuantity(beforeDebutProduct.getQuantity() - debutQuentity);
		beforeDebutProduct = productInventoryDao.saveAndFlush(beforeDebutProduct);
		ProductInventoryAudit productInventoryAudit = new ProductInventoryAudit(username, debutQuentity,
				InventoryStatus.DEBUT);
		productInventoryAudit.setProductInventory(beforeDebutProduct);
		productInventoryAuditDao.save(productInventoryAudit);
		return "success debut produt";
	}
	 
	public record inventoryCompansationDto(String orderId, String message) {}
	
	public record PaymentFailDto(String orderId, String message) {}
	@RabbitListener(queues = "payment.fail.queue")
	@Transactional
	public void inventoryComapnsateFromPaymentFail(PaymentFailDto paymentFailDto) {
		System.out.println("Inventory Compasation...");
		
		Optional<InventoryCompansate> compansateOptional = 
				inventoryCompansateDao.findByOrderId(paymentFailDto.orderId);

		if(compansateOptional.isPresent()) {
			InventoryCompansate inventoryCompansate = compansateOptional
					.get();
			if("Compansate".equals(inventoryCompansate.getStatus())) {
				System.out.println("Already Compansated!!");
				return;
			}
			List<Product> compansateList = inventoryCompansate.getProducts();
			
			for(Product product:compansateList) {
				
				ProductInventory productInventory = productInventoryDao
						.findByName(product.name()).get();
				int currentQuantity = productInventory.getQuantity();
				int updatedQuantity = currentQuantity + product.quantity();
				productInventory.setQuantity(updatedQuantity);
				ProductInventoryAudit productInventoryAudit = 
						new  ProductInventoryAudit();
				productInventoryAudit.setDebutQuantity(product.quantity());
				productInventoryAudit.setInventoryStatus(InventoryStatus.COMPANSATE);
				productInventoryAudit.setProductInventory(productInventory);	
				productInventoryAuditDao.save(productInventoryAudit);
			}
			inventoryCompansate.setStatus("Compansate");
			inventoryCompansateDao.save(inventoryCompansate);
			
			var comDto = new inventoryCompansationDto(
					paymentFailDto.orderId, 
					paymentFailDto.message);
			
			rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.INVENTORY_COMPANSATION_BININGE_KEY,
					comDto);
					
		}
	}

	private ProductInventory findProductInventoryById(long id) {
		return productInventoryDao.findById(id).orElseThrow(ProductNotFoundException::new);
	}

}
