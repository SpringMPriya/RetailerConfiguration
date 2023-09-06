package com.springboot.swagger.controller;

import com.springboot.swagger.dao.CustomerDAO;
import com.springboot.swagger.dao.TransactionDAO;
import com.springboot.swagger.ds.Customer;
import com.springboot.swagger.ds.Transaction;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/retail")
public class RetailerController {

	@Autowired
	private CustomerDAO customerDao;

	@Autowired
	private TransactionDAO transactionDAO;

	@GetMapping(path = "/getCustomers")
	@ApiOperation(value = "Retrieves the list of customers under the Retailer", response = ResponseEntity.class)
	public ResponseEntity<List<Customer>> getCustomers() {
		return ResponseEntity.ok()
				.body(customerDao.findAll());
	}

	@PostMapping(path = "/placeOrder",consumes = APPLICATION_JSON_VALUE,  produces = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Place the order with the Retailer", response = ResponseEntity.class)
	public ResponseEntity<Customer> placeOrder(@RequestParam String customerId, @RequestParam double amount) {
		Customer customer = customerDao.findById(customerId).get();
		List<Transaction> transactions = customer.getTransactions();
		Transaction txn = new Transaction();
		txn.setTransactionDate(new Date());
		txn.setTransactionAmount(amount);
		txn.setRewardPoints(calculateRewardPoints(amount));
		txn.setCustomer(customer);
		transactions.add(txn);
		customer.setTransactions(transactions);
		customerDao.save(customer);
	    return ResponseEntity.ok().body(customer);
	}

	private int calculateRewardPoints(double amount){
		int rewardPoints = 0;
		int spent = (int)amount;
		int hundreds = spent/100;
		if (hundreds >= 1){
			int doublePoints = spent - 100;
			rewardPoints += (doublePoints *2);
			spent = spent - ( hundreds* 100);
		}
		if ((hundreds >= 1 && spent > 1) || (spent > 50)){
			rewardPoints += 50;
		}
		return rewardPoints;
	}
	@GetMapping(path = "/getRewardPointsHistory")
	@ApiOperation(value = "Retrieves the  reward points earned for each customer per month", response = ResponseEntity.class)
	public ResponseEntity<Map<String,Long>> getRewardPointsHistory() {
		List<Customer> customers = customerDao.findAll();
		Map<String,Long> rewardsSummary = new HashMap<>();
		customers.stream().forEach(customer ->
		{
			Map<Customer, IntSummaryStatistics> rewards =	customer.getTransactions().stream().
					collect(
							groupingBy(Transaction::getCustomer, summarizingInt(Transaction::getRewardPoints)));
			rewards.forEach((key, value) -> rewardsSummary.put(key.getName(), value.getSum()));
		});
		return ResponseEntity.ok().body(rewardsSummary);
	}
}
