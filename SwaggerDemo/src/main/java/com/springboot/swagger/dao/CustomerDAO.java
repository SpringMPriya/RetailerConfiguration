package com.springboot.swagger.dao;

import com.springboot.swagger.ds.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerDAO extends CrudRepository<Customer, String> {
   List<Customer> findAll();

}
