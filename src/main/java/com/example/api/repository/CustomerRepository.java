package com.example.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.api.domain.Customer;
import com.example.api.repository.query.CustomerRepositoryQuery;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryQuery, PagingAndSortingRepository<Customer, Long> {

	Page<Customer> findAllByOrderByNameAsc(Pageable pageable);

}