package com.metacore.address.address;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metacore.address.address.resource.AddressResourceRequest;
import com.metacore.address.address.resource.AddressResourceResponse;

@RestController
@RequestMapping("/v1/address")
public class AddressController {
	
	@Autowired
	AddressAssembler addressAssembler;
	
    @Autowired
    AddressService addressService;
  
  @PostMapping("/notes")
  public AddressResourceResponse create(@Valid @RequestBody AddressResourceRequest request) {
	  return toResponse(create(toAddress(request)));
  }
  
  private Address toAddress(AddressResourceRequest request) {
	  return addressAssembler.toDomainObject(request);
  }
  
  private Address create(Address address) {
	  return addressService.create(address);
  }
  
  private AddressResourceResponse toResponse(Address address) {
	  return addressAssembler.toResponseObject(address);
  }
}