package com.example.demo.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.jwt.TokenProvider;

@Configuration

public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationEntryPoint entryPoint;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
	throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
	throws Exception{
		//http.httpBasic(Customizer.withDefaults());
		http.exceptionHandling(c -> c.authenticationEntryPoint(entryPoint));
		http.sessionManagement( c -> c
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(c -> c.disable());
		http.authorizeHttpRequests( c -> {
			c.requestMatchers("/api/auth/**").permitAll();
			c.anyRequest().authenticated();
		});
		http.oauth2ResourceServer(oauth2 ->{
			oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()));
		});
		
		return http.build();
	}
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey=new SecretKeySpec(TokenProvider
				.SECRET_KEY.getBytes(),"HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}
	
	
}






