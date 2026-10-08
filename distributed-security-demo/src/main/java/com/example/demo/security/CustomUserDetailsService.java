package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		UserDetails user=userDao.findByUsername(username)
				.map(SecurityUser::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + " Not Found!"));
		System.out.println("userdetails service:"+user.getUsername());
		return user;
	}
	
	

}
