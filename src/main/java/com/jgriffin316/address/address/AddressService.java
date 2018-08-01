package com.jgriffin316.address.address;

import static org.apache.commons.codec.digest.DigestUtils.shaHex;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jgriffin316.address.domain.Address;
import com.jgriffin316.address.domain.AddressRepository;

@Component
public class AddressService {
  @Autowired
  AddressRepository addressRepository;

  public Address create(Address address) {
    address.setId(generateId(address));
    return addressRepository.save(address);
  }

  String generateId(Address address) {
    return shaHex(address.getSeedForSha1());
  }

  public Optional<Address> get(String id) {
    return addressRepository.findById(id);
  }

  public void delete(String id) {
    addressRepository.deleteById(id);
  }
}
