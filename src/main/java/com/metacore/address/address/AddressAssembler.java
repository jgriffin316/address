package com.metacore.address.address;

import org.springframework.stereotype.Component;

import com.metacore.address.address.resource.AddressResourceRequest;
import com.metacore.address.address.resource.AddressResourceResponse;

@Component
public class AddressAssembler {
  Address toDomainObject(AddressResourceRequest request) {
	  Address address = new Address();
	  address.setNumber(request.number);
	  address.setUnit(request.unit);
	  address.setStreet(request.street);
	  address.setCity(request.city);
	  address.setState(request.state);
	  address.setCountry(request.country);
	  return address;
  }
  
  AddressResourceResponse toResponseObject(Address address) {
	  AddressResourceResponse response = new AddressResourceResponse();
	  response.id = address.getId();
	  response.number = address.getNumber();
	  response.unit = address.getUnit();
	  response.street = address.getStreet();
	  response.state = address.getState();
	  response.country = address.getCountry();
	  return response;
  }
}
