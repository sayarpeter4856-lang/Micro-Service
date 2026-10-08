package com.example.demo.cart;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.example.demo.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
public class CartService {

    private final CartRepository cartRepository;
    
    public void decreaseQuantity(long id) {
    		Cart cart=getMyCart();
    		cart.getItems().stream()
    			.map(item ->{
    				if(item.getId() == id  && item.getQuantity() > 1) {
    					item.setQuantity(item.getQuantity() - 1);
    				}
    				return item;
    			})
    			.collect(Collectors.toSet());
    		cartRepository.save(cart);
    }
    
    
    public void increaseQuantity(long id) {
		Cart cart=getMyCart();
		cart.getItems().stream()
		 .map(item ->{
			 if(item.getId() == id) {
				 item.setQuantity(item.getQuantity() + 1);
			 }
			 return item;
		 }).collect(Collectors.toSet());
		cartRepository.save(cart);
	}

    
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    public void clearCart() {
    		cartRepository.deleteById(getBrowserSessionId());
    }

    public Cart getMyCart() {
        String sessionId = getBrowserSessionId();
        return cartRepository.findById(sessionId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(sessionId);
                    return newCart;
                });
    }

    public void addToCart(ProductDto productDto) {
        String sessionId = getBrowserSessionId();
        Cart cart = cartRepository.findById(sessionId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(sessionId);
                    return newCart;
                });

        cart.addItem(toCartItem(productDto));
        cartRepository.save(cart);
    }

    private CartItem toCartItem(ProductDto productDto) {
        return new CartItem(
                productDto.id(),
                productDto.name(),
                1,
                productDto.price()
        );
    }

    private String getBrowserSessionId() {
        return RequestContextHolder
                .currentRequestAttributes()
                .getSessionId();
    }
    
    public void deleteCart() {
    	cartRepository.deleteById(getBrowserSessionId());
    }
    
}