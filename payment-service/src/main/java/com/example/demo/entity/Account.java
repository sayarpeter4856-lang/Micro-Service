package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String accountNumber;
	private String username;
	private BigDecimal amount;
	@OneToMany(mappedBy = "account",
			cascade = CascadeType.PERSIST)
	private List<Transaction> transactions=
			new ArrayList<>();
	
	public void addTransaction(Transaction tr) {
		tr.setAccount(this);
		transactions.add(tr);
	}
	

}
