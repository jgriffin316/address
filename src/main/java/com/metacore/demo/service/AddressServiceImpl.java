package com.metacore.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacore.demo.domain.Address;
import com.metacore.demo.repository.AddressRepository;

@Component
public class AddressServiceImpl implements AddressService {
	@Autowired
	AddressRepository addressRepository;

	@Override
	public Address create(Address address) {
		address.setId(UUID.randomUUID().toString());
		return addressRepository.save(address);
	}
}
