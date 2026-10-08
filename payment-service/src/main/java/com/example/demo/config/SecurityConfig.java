package com.example.demo.config;


import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {
	@Autowired
	private AuthenticationEntryPoint entryPoint;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
	throws Exception{
		//http.httpBasic(Customizer.withDefaults());
		http.exceptionHandling(c -> c.authenticationEntryPoint(entryPoint));
		http.sessionManagement( c -> c
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(c -> c.disable());
		http.authorizeHttpRequests( c -> {
			c.anyRequest().authenticated();
		});
		http.oauth2ResourceServer(oauth2 ->{
			oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()));
		});
		
		return http.build();
	}
	public static final String SECRET_KEY="my-256-bit-extremely-secret-key-for-jwt-singing-key";
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey=new SecretKeySpec(SECRET_KEY
				.getBytes(),"HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}
	

}
