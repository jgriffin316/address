package com.jgriffin316.address.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jgriffin316.address.domain.Address;

public class AddressTest {

  Address address;

  @Before
  public void setup() {
    address = new Address();
  }

  @Test
  public void number_validNumber_expectSuccess() {
    address.setNumber("123");
    assertEquals("123", address.getNumber());
  }

  @Test
  public void number_numberWithSpaces_expectSuccess() {
    address.setNumber("  123  ");
    assertEquals("123", address.getNumber());
  }

  @Test
  public void number_numberWithLetter_expectSuccess() {
    address.setNumber("123D");
    assertEquals("123D", address.getNumber());
  }

  @Test
  public void number_numberWithNumbersAndLetter_expectSuccess() {
    address.setNumber(" 123 D ");
    assertEquals("123D", address.getNumber());
  }
}
