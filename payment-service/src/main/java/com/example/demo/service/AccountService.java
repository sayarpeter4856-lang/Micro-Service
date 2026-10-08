package com.example.demo.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.config.PaymentRabbitMqConfig;
import com.example.demo.dao.AccountDao;
import com.example.demo.dao.TransactionDao;
import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.exception.AccountNoFoundException;
import com.example.demo.exception.InSufficientAmountException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountDao accountDao;
	private final TransactionDao transactionDao;
	private final RabbitTemplate rabbitTemplate;

	public record PaymentRequest(String fromUser, String fromAccountNumber, double amount, String orderCode) {
	
	}

	@RabbitListener(queues = "payment.processing.queue")
	@Transactional
	public void paymentRequestListenerFromInventory(PaymentRequest request) {
		try {
			withdraw(request.fromUser, request.fromAccountNumber, request.amount, TransactionType.TRANSFER);
			deposit("oric", "AFFABLE-ORIC2233", request.amount, TransactionType.TRANSFER);

			// exchange,routing-key,body
			var paymentSuccessRequest = new PaymentSuccessRequest(request.orderCode);
			rabbitTemplate.convertAndSend(PaymentRabbitMqConfig.EXCHANGE, PaymentRabbitMqConfig.PAYMENT_SUCCESS_ROUTING_KEY,
					paymentSuccessRequest);
		} catch (Exception e) {
			var paymentFail = new PaymentFailDto(request.orderCode, getMessage(e));
			rabbitTemplate.convertAndSend(PaymentRabbitMqConfig.EXCHANGE,
					PaymentRabbitMqConfig.PAYMENT_FAIL_ROUTING_KEY,
					paymentFail);
		}
	}

	private String getMessage(Exception e) {
		String message = "";
		if(e instanceof AccountNoFoundException) {
			message = "Account not Found!";
		}
		if (e instanceof InSufficientAmountException) {
			message = "Insufficient Amount!";
		}
		return message;
	}

	public record PaymentSuccessRequest(String orderCode) {
	}

	public record PaymentFailDto(String orderId, String message) {
	}

	@Transactional
	public String transfer(String fromUsername, String fromAccountNumber, String toUsername, String toAccountNumeber,
			double amount, TransactionType type) {

		withdraw(fromUsername, fromAccountNumber, amount, type);

		deposit(toUsername, toAccountNumeber, amount, type);
		return "success tranfer.";
	}

	public Map<String, String> withdraw(String username, String accountNumber, double amount,
			TransactionType transactionType) {

		Account account = accountDao.findByUsernameAndAccountNumber(username.trim(), accountNumber.trim())
				.orElseThrow(AccountNoFoundException::new);
		if (account.getAmount().doubleValue() < amount) {
			throw new InSufficientAmountException();
		}
		account.setAmount(account.getAmount().subtract(BigDecimal.valueOf(amount)));
		Transaction ts = new Transaction();
		ts.setTransactionType(transactionType);
		account.addTransaction(transactionDao.save(ts));
		account = accountDao.saveAndFlush(account);
		return Map.of("account-number", account.getAccountNumber(), "username", account.getUsername(), "transaction",
				"Withdraw", "currentAmount", String.valueOf(account.getAmount()));
	}

	public Map<String, String> deposit(String username, String accountNumber, double amount,
			TransactionType transactionType) {
		Account account = accountDao.findByUsernameAndAccountNumber(username, accountNumber)
				.orElseThrow(AccountNoFoundException::new);
		Transaction ts = new Transaction();
		ts.setTransactionType(transactionType);
		account.setAmount(account.getAmount().add(BigDecimal.valueOf(amount)));
		account.addTransaction(transactionDao.save(ts));
		account = accountDao.saveAndFlush(account);
		return Map.of("account-number", account.getAccountNumber(), "username", account.getUsername(), "transaction",
				"Deposit", "currentAmount", String.valueOf(account.getAmount()));
	}

	public String createAccount(String username) {
		Account account = new Account();
		account.setAccountNumber(generatedAccountNumber(username));
		account.setUsername(username);
		account.setAmount(BigDecimal.valueOf(0));
		Transaction transaction = new Transaction();
		transaction.setTransactionType(TransactionType.CREATE_ACCOUNT);
		account.addTransaction(transactionDao.save(transaction));
		accountDao.save(account);
		return "%s successfully created %s account.".formatted(username, account.getAccountNumber());
	}

	private String generatedAccountNumber(String username) {
		SecureRandom randon = new SecureRandom();
		int num = randon.nextInt(100000) + 100000;// 0-99999 + 100000 - 6 digits
		return new StringBuilder().append("AFFABLEBEAN-").append(username.toUpperCase()).append(num).toString();

	}

}
