package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.ProductInventory;
import com.example.demo.service.ProductInventoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/ui/inventory")
@RequiredArgsConstructor
public class InventoryController {
	private final ProductInventoryService productInventoryService;
	// http://localhost:8080/ui/inventory/products
	@GetMapping("/products")
	public String listAll(Model model){
		model.addAttribute("products",
				productInventoryService.listAllProductInventory());
		return "home";
	}
	
	
}
