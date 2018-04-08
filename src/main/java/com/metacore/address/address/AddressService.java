package com.metacore.address.address;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressService {
  @Autowired
  AddressRepository addressRepository;

  public Address create(Address address) {
    address.setId(UUID.randomUUID().toString());
    return addressRepository.save(address);
  }

  public Optional<Address> get(String id) {
    return addressRepository.findById(id);
  }
}
