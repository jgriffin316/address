package com.metacore.address.address.resource;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class AddressResourceResponse extends AddressResourceRequest {
  public String id;
  public List<AddressResourceError> errors;
}
