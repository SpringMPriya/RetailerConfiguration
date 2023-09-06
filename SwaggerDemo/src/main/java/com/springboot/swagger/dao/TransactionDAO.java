package com.springboot.swagger.dao;

import com.springboot.swagger.ds.Customer;
import com.springboot.swagger.ds.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionDAO extends CrudRepository<Transaction, Integer> {
    List<Transaction> findAll();
}
