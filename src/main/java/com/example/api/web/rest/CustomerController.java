package com.example.api.web.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	public Page<Customer> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}

	@PostMapping
	public Customer save(@Valid @RequestBody Customer customer) {
		processAddress(customer);
		return service.save(customer);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Customer customer) {
		processAddress(customer);
		customer = service.update(customer);
		if(customer.getId() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			return ResponseEntity.ok().body(customer);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> delete(@Valid @RequestBody Customer customer) {
		Optional<Customer> search = service.findById(customer.getId());
		if(search.isPresent()) {
			service.delete(customer);
			return ResponseEntity.ok().body(customer.toString() + " deleted with success!");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		Optional<Customer> search = service.findById(id);
		if(search.isPresent()) {
			service.deleteById(id);
			return ResponseEntity.ok().body("ID " + id + " deleted with success!");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	private Address searchAddressInfoByCep(String cep) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json", Address.class);
	}

	private void processAddress(Customer customer) {

		if(customer.getEndereco() != null){
			Set<Address> addressList = new HashSet(); 
			customer.getEndereco().forEach(address -> {
				Long id = address.getId();
				String complement = address.getComplemento();
				Address founded = searchAddressInfoByCep(address.getCep());
				founded.setComplemento(complement);
				founded.setId(id);
				addressList.add(founded);
			});
			customer.setEndereco(addressList);
		}

	}

}