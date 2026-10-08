package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Transaction;

public interface TransactionDao extends JpaRepository<Transaction,Long>{

}
