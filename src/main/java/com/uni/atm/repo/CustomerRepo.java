package com.uni.atm.repo;

import org.springframework.data.repository.CrudRepository;

import com.uni.atm.domain.Customer;

public interface CustomerRepo extends CrudRepository<Customer, Long> {

}
