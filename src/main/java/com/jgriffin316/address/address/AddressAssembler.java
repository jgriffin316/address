package com.jgriffin316.address.address;

import com.jgriffin316.address.address.resource.AddressResourceRequest;
import com.jgriffin316.address.address.resource.AddressResourceResponse;
import com.jgriffin316.address.domain.Address;

public class AddressAssembler {
  static Address toDomainObject(AddressResourceRequest request) {
    Address address = new Address();
    address.setNumber(request.number);
    address.setUnit(request.unit);
    address.setStreet(request.street);
    address.setCity(request.city);
    address.setZip(request.zip);
    address.setState(request.state);
    address.setCountry(request.country);
    return address;
  }

  public static AddressResourceResponse toResponseObject(Address address) {
    AddressResourceResponse response = new AddressResourceResponse();
    response.id = address.getId();
    response.number = address.getNumber();
    response.unit = address.getUnit();
    response.street = address.getStreet();
    response.city = address.getCity();
    response.zip = address.getZip();
    response.state = address.getState();
    response.country = address.getCountry();
    return response;
  }
}
