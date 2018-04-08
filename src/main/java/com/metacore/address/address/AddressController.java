package com.metacore.address.address;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
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
  AddressService addressService;

  @PostMapping
  public AddressResourceResponse create(@Valid @RequestBody AddressResourceRequest request) {
    return toResponse(create(toAddress(request)));
  }

  @GetMapping("/{id}")
  public AddressResourceResponse get(@NotBlank @PathVariable String id) {
    return toResponse(findById(id));
  }

  private Address create(Address address) {
    return addressService.create(address);
  }

  private Optional<Address> findById(String id) {
    return addressService.get(id);
  }

  private Address toAddress(AddressResourceRequest request) {
    return AddressAssembler.toDomainObject(request);
  }

  private AddressResourceResponse toResponse(Address address) {
    // URI location = URI.create(createLocation(address.getId()));
    // ServerResponse.created(location).build();

    return AddressAssembler.toResponseObject(address);
  }

  // private String createLocation(String id) {
  // return linkTo(AddressController.class).slash(id).toString();
  // }

  private AddressResourceResponse toResponse(Optional<Address> address) {
    if (address.isPresent()) {
      return toResponse(address.get());
    } else {
      throw new NotFoundException();
    }
  }
}
