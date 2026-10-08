package com.example.demo.controller;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.cart.CartItem;
import com.example.demo.cart.CartService;
import com.example.demo.dto.ProductDto;
import com.example.demo.order.success.OrderInfo;
import com.example.demo.order.success.OrderInfoReposity;
import com.example.demo.order.success.OrderStatus;
import com.example.demo.service.AffableBeanService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/affable-bean/ui")
@RequiredArgsConstructor
public class AffableBeanController {
	private final AffableBeanService affableService;
	private final CartService cartService;
	
	@GetMapping("/checkout-view")
	public String checkoutView() {
		return "checkout-view";
	}
	// /affable-bean/ui/checkout-view
	@PostMapping("/checkout-view")
	public String processCheckout(
			@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("accountNumber")String accountNumber) {
		String orderId = UUID.randomUUID().toString();
		affableService.checkout(username, password, accountNumber,orderId);
		cartService.deleteCart();
		return "redirect:/affable-bean/ui/order-info/" + orderId;
	}
	
	// success method
	// affable-bean/ui/order-info
	// //affable-bean/ui/cart-view
	
	@RequestMapping("/order-info/{orderId}")
	public String orderStatusInfo(@PathVariable("orderId")String orderId, Model model) {
		OrderInfo orderInfo = orderInfoReposity.findByOrderId(orderId)
				.orElse(null);
		if(Objects.isNull(orderInfo)) {
			model.addAttribute("orderId", orderId);
			return "order-loading";
		}else if (orderInfo.getOrderStatus().equals(OrderStatus.ORDER_SUCCESS)) {
			model.addAttribute("orderId", orderInfo.getId());
			model.addAttribute("error", false);
			model.addAttribute("orderStatus", "Order Success");
			return "success";
		}else if (orderInfo.getOrderStatus().equals(OrderStatus.ORDER_FAIL)) {
			model.addAttribute("OrderId", orderInfo.getId());
			model.addAttribute("error", true);
			model.addAttribute("orderStatus", orderInfo.getMessage());
			return "success";
		}else {
			model.addAttribute("orderId", orderInfo.getOrderId());
			model.addAttribute("orderStatus", "Unknown Error!");
			return "success";
		}
		
	}
	
	@Autowired
	private OrderInfoReposity orderInfoReposity;
	
	@GetMapping("/cart-view")
	public String viewCart(Model model) {
		model.addAttribute("cartItems", cartService
				.getMyCart()
				.getItems());		
		return "cart-view";
	}
	// /affable-bean/ui/decrease-quantity?id=
	@GetMapping("/decrease-quantity")
	public String decreaseQuantity(@RequestParam("id")long id) {
		cartService.decreaseQuantity(id);
		return "redirect:/affable-bean/ui/cart-view";
	}
	// /affable-bean/ui/increase-quantity?id=
	@GetMapping("/increase-quantity")
	public String increaseQuantity(@RequestParam("id")long id) {
		cartService.increaseQuantity(id);
		return "redirect:/affable-bean/ui/cart-view";
	}
	// /affable-bean/ui/products?name=
	@GetMapping("/products")
	public String listProducts(@RequestParam("name")String name,
			Model model) {
		model.addAttribute("products",affableService
				.listAllProducts(name));
		currentCategoryName=name;
		return "products";
	}
	// /affable-bean/ui/clear-cart
	@GetMapping("/clear-cart")
	public String clearCart() {
		cartService.clearCart();
		return "redirect:/affable-bean/ui/cart-view";
	}
	private String currentCategoryName;
	// /affable-bean/ui/add-to-cart?id=2
	@GetMapping("/add-to-cart")
	public String addToCart(@RequestParam("id")long id) {
		ProductDto productDto=affableService.getProductById(id);
		cartService.addToCart(productDto);
		return "redirect:/affable-bean/ui/products?name="+currentCategoryName;
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@ModelAttribute("totalCost")
	public double totalCost() {
		return cartService.getMyCart()
				.getItems()
				.stream()
				.map(item -> item.getQuantity() * item.getPrice())
				.mapToDouble(Double::doubleValue)
				.sum();
	}
	@ModelAttribute("itemSize")
	public int itemSize() {
		return cartService.getMyCart()
				.getItems()
				.stream()
				.map(CartItem::getQuantity) //Stream<Integer>
				.mapToInt(Integer::intValue) //IntStream
				.sum();
				
	}
	@ModelAttribute("cartSize")
	public int cartSize() {
		return cartService.getMyCart().getItems().size();
	}

}
