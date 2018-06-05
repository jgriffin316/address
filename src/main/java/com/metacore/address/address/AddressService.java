package com.metacore.address.address;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacore.address.domain.Address;
import com.metacore.address.domain.AddressRepository;

@Component
public class AddressService {
  @Autowired
  AddressRepository addressRepository;

  public Address create(Address address) {
    address.setId(generateId(address));
    return addressRepository.save(address);
  }

  String generateId(Address address) {
    return DigestUtils.shaHex(address.getSeedForSha1());
  }

  public Optional<Address> get(String id) {
    return addressRepository.findById(id);
  }

  public void delete(String id) {
    addressRepository.deleteById(id);
  }
}
