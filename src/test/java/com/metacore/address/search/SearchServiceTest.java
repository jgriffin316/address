package com.metacore.address.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.metacore.address.domain.Address;
import com.metacore.address.domain.AddressRepository;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {
  @Spy
  AddressRepository addressRepository;

  @InjectMocks
  SearchService searchService;

  @Test(expected = NullPointerException.class)
  public void search_withNullCriteria_expectNullPointerException() {
    assertEquals(0, searchService.search(null).count());
  }

  @Test
  public void search_singleOperator_expectEmptyResult() {
    List<Operation> operations = new ArrayList<>();
    operations.add(new Operation("x", ParserOperator.EQ, Arrays.asList("1")));
    Stream<Address> actual = searchService.search(operations);
    assertEquals(0, actual.count());
  }
}
