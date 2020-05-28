package com.example.api.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.api.domain.Address;
import com.example.api.repository.query.AddressRepositoryQuery;

@Repository
public class AddressRepositoryImpl implements AddressRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Address update(Address address) {
		Address addrss = entityManager.merge(address);
	    return addrss;
	}

}