package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.RoleDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.jwt.TokenProvider;
import com.nimbusds.jose.JOSEException;

@Service
public class AuthService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenProvider tokenProvider;
	
	public String login(String username,String password)throws JOSEException {
		var auth=new UsernamePasswordAuthenticationToken(username, password);
		var authentication=authenticationManager
				.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return tokenProvider.createToken(authentication.getName());
//		return "%s successfully logined."
//				.formatted(authentication.getName());
	}
	
	
	
	public String register(String username,String password,String email) {
		User user=new User(username,
				passwordEncoder.encode(password),
				email);
		roleDao.findByRoleName("ROLE_USER")
		.ifPresentOrElse(role ->{
			user.getRoles().add(role);
			userDao.save(user);
		}, ()->{
			Role role=new Role();
			role.setRoleName("ROLE_USER");
			user.getRoles().add(roleDao.save(role));
			userDao.save(user);
		});
		return "%s registered successfully!".formatted(username);
	}

}




