package com.metacore.address.search;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {
  @InjectMocks
  SearchService searchService;

  @Test
  public void search_withNullCriteria_expectEmptySet() {
    assertTrue(searchService.search(null).isEmpty());
  }

  @Test
  public void buildCriteria_withSingleArgument_expect() {

  }

}
