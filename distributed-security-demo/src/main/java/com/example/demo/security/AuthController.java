package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	record RegisterDto(String username,String password,String email) {}
	record LoginDto(String username,String password) {}
	
	record LoginResponse(String token) {}
	// http://localhost:8080/api/auth/login
	@PostMapping("/login")
	public ResponseEntity<LoginResponse>  login(@RequestBody LoginDto dto)throws JOSEException{
		String returnString= authService.login(dto.username, dto.password);
		return ResponseEntity.ok().body(new LoginResponse(returnString));
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto dto){
		String returnString=authService.register(dto.username,
				dto.password,
				dto.email);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(returnString);
	}
	
	
	
	
	

}
