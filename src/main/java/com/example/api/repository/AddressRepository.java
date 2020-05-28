package com.example.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.api.domain.Address;
import com.example.api.repository.query.AddressRepositoryQuery;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long>, AddressRepositoryQuery, PagingAndSortingRepository<Address, Long> {

}