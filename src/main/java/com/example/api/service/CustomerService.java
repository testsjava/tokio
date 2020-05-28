package com.example.api.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.repository.AddressRepository;
import com.example.api.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

	private CustomerRepository repository;

	private AddressRepository addressRepository;

	@Autowired
	public CustomerService(CustomerRepository repository, AddressRepository addressRepository) {
		this.repository = repository;
		this.addressRepository = addressRepository;
	}

	public Page<Customer> findAll(Pageable pageable) {
		return repository.findAllByOrderByNameAsc(pageable);
	}

	public Optional<Customer> findById(Long id) {
		return repository.findById(id);
	}

	public Customer save(Customer customer) {
		if(customer.getId() != null) {
			customer.setId(null);
		}
		Customer saved = repository.save(customer);
		Set<Address> addressList = new HashSet();
		if(customer.getEndereco() != null) {
			customer.getEndereco().forEach(address -> {
				address.setCustomer(saved);
				addressList.add(addressRepository.save(address));
			});
			saved.setEndereco(addressList);
		}
		return saved;
	}

	public Customer update(Customer customer) {
		if(customer.getId() != null) {
			Optional<Customer> founded = repository.findById(customer.getId());
			if(founded.isPresent()) {
				Customer updated = repository.update(customer);
				
				Set<Address> addressList = new HashSet();
				if(customer.getEndereco() != null) {
					customer.getEndereco().forEach(address -> {
						address.setCustomer(updated);
						if(address.getId() == null) {
							addressList.add(addressRepository.save(address));
						}else {
							addressList.add(addressRepository.update(address));
						}
					});
					updated.setEndereco(addressList);
				}
				
				return repository.update(customer);
			}
		}
		customer.setId(null);
		return customer;
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	public void delete(Customer customer) {
		repository.delete(customer);
	}

}