package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import java.util.List;


public interface AccountDao extends JpaRepository<Account,Long>{
	
	Optional<Account> findByUsernameAndAccountNumber(String username,
			String accountNumber);

}
