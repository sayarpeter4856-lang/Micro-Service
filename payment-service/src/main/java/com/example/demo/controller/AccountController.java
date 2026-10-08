package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TransactionType;
import com.example.demo.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class AccountController {
	private final AccountService accountService;
	
	record AccountCreateRequest(String username) {}
	record DepositWithdrawRequest(String username,
			String accountNumber,
			double amount) {}
	@PostMapping("/withdraw")
	public Map<String,String> withdraw(@RequestBody DepositWithdrawRequest request){
		return accountService.withdraw(request.username,
				request.accountNumber, request.amount,
				TransactionType.WITHDRAW);
	}
	@PostMapping("/deposit")
	public Map<String,String> deposit(@RequestBody DepositWithdrawRequest request){
		return accountService.deposit(request.username, request.accountNumber,
				request.amount,TransactionType.DEPOSIT);
	}
	record TransferRequest(
			String fromUsername,
			String fromAccountNumber,
			String toUsername,
			String toAccountNumber,
			double amount) {}
	@PostMapping("/transfer")
	public ResponseEntity<String> transfer(@RequestBody TransferRequest request){
		String returnString=accountService
				.transfer(request.fromUsername,
						request.fromAccountNumber,
						request.toUsername,
						request.toAccountNumber,
						request.amount,TransactionType.TRANSFER);
		return ResponseEntity.ok(returnString);
		
	}
	
	// http://localhost:8080/api/payment/open-account
	@PostMapping("/open-account")
	public ResponseEntity<String> 
		createAccount(@RequestBody AccountCreateRequest request){
		String returnString= accountService.createAccount(request.username);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(returnString);
	}

}







