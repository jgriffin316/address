package com.metacore.address.search;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.ResponseEntity.badRequest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metacore.address.address.AddressAssembler;
import com.metacore.address.address.resource.AddressResourceError;
import com.metacore.address.address.resource.AddressResourceResponse;
import com.metacore.address.domain.Address;

@RestController
@RequestMapping("/v1/address")
public class SearchController {
  SearchParser queryParser;

  @Autowired
  SearchService searchService;

  public SearchController() {
    queryParser = new SearchParser();
    queryParser.setPropertyList(Arrays.asList("number", "unit", "street", "city", "zip", "state", "country"));
  }

  @GetMapping("")
  public ResponseEntity<List<AddressResourceResponse>> search(
      @RequestParam(name = "query", required = true) String query) throws ParseException, Exception {
    return new ResponseEntity<>(retrieveResponse(query), HttpStatus.OK);
  }

  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<AddressResourceResponse> exceptionHandler(RuntimeException ex) {
    return sendErrorResponse(toAddressResourceErrorList(ex));
  }

  ResponseEntity<AddressResourceResponse> sendErrorResponse(List<AddressResourceError> errors) {
    AddressResourceResponse body = new AddressResourceResponse();
    body.errors = errors;
    return badRequest().contentType(APPLICATION_JSON_UTF8).body(body);
  }

  List<AddressResourceError> toAddressResourceErrorList(RuntimeException error) {
    return Arrays.asList(createAddressResourceError(error));
  }

  AddressResourceError createAddressResourceError(RuntimeException error) {
    return new AddressResourceError("query", error.getMessage());
  }

  List<AddressResourceResponse> retrieveResponse(String query) throws ParseException, Exception {
    return searchService.search(queryParser.parse(query)).map(it -> toResponse(it)).collect(toList());
  }

  AddressResourceResponse toResponse(Address address) {
    return AddressAssembler.toResponseObject(address);
  }
}
