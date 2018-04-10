package com.metacore.address.address;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<AddressResourceResponse> create(@Valid @RequestBody AddressResourceRequest request) {
    AddressResourceResponse body = toResponse(create(toAddress(request)));
    return created(createLocation(body.id)).contentType(MediaType.APPLICATION_JSON_UTF8).body(body);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AddressResourceResponse> get(@NotBlank @PathVariable String id) {
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
    return AddressAssembler.toResponseObject(address);
  }

  private URI createLocation(String id) {
    return URI.create(linkTo(AddressController.class).slash(id).toString());
  }

  private ResponseEntity<AddressResourceResponse> toResponse(Optional<Address> address) {
    return address.isPresent() ? ok().body(toResponse(address.get())) : notFound().build();
  }
}
