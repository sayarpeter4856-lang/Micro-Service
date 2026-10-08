package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.service.registry.ImportHttpServices;

import com.example.demo.client.AuthClient;
import com.example.demo.client.OrderClient;
import com.example.demo.client.ProductClient;

@SpringBootApplication
@ImportHttpServices({ProductClient.class,OrderClient.class,AuthClient.class})
public class AffableBeanUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AffableBeanUiApplication.class, args);
	}

}
