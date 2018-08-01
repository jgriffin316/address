package com.jgriffin316.address.address;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.jgriffin316.address.address.resource.AddressResourceError;
import com.jgriffin316.address.address.resource.AddressResourceRequest;
import com.jgriffin316.address.address.resource.AddressResourceResponse;
import com.jgriffin316.address.domain.Address;

@RestController
@RequestMapping("/v1/address")
public class AddressController {

  @Autowired
  AddressService addressService;

  @PostMapping
  public ResponseEntity<AddressResourceResponse> create(@Valid @RequestBody AddressResourceRequest request) {
    return sendCreatedResponse(toResponse(create(toAddress(request))));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  ResponseEntity<AddressResourceResponse> exceptionHandler(WebExchangeBindException ex) {
    return sendErrorResponse(toAddressResourceErrorList(ex.getAllErrors()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AddressResourceResponse> get(@NotBlank @PathVariable String id) {
    return toResponse(findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<AddressResourceResponse> delete(@NotBlank @PathVariable String id) {
    addressService.delete(id);
    return accepted().build();
  }

  Address create(Address address) {
    return addressService.create(address);
  }

  Optional<Address> findById(String id) {
    return addressService.get(id);
  }

  Address toAddress(AddressResourceRequest request) {
    return AddressAssembler.toDomainObject(request);
  }

  AddressResourceResponse toResponse(Address address) {
    return AddressAssembler.toResponseObject(address);
  }

  List<AddressResourceError> toAddressResourceErrorList(List<ObjectError> errors) {
    return errors.stream().map(it -> createAddressResourceError((FieldError) it)).collect(toList());
  }

  AddressResourceError createAddressResourceError(FieldError error) {
    return new AddressResourceError(error.getField(), error.getDefaultMessage());
  }

  ResponseEntity<AddressResourceResponse> sendErrorResponse(List<AddressResourceError> errors) {
    AddressResourceResponse body = new AddressResourceResponse();
    body.errors = errors;
    return badRequest().contentType(APPLICATION_JSON_UTF8).body(body);
  }

  ResponseEntity<AddressResourceResponse> sendCreatedResponse(AddressResourceResponse body) {
    return created(createLocation(body.id)).contentType(APPLICATION_JSON_UTF8).body(body);
  }

  URI createLocation(String id) {
    return URI.create(linkTo(AddressController.class).slash(id).toString());
  }

  ResponseEntity<AddressResourceResponse> toResponse(Optional<Address> address) {
    return address.isPresent() ? ok().body(toResponse(address.get())) : notFound().build();
  }
}
