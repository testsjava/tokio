package com.example.api.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.api.domain.Customer;
import com.example.api.repository.query.CustomerRepositoryQuery;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Customer update(Customer customer) {
		Customer cstmr = entityManager.merge(customer);
	    return cstmr;
	}

}