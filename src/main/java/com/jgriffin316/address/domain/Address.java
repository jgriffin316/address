package com.jgriffin316.address.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Address {
  @Id
  private String id;

  private String number;
  private String unit;
  private String street;
  private String city;
  private String zip;
  private String state;
  private String country;

  @CreationTimestamp
  private Timestamp createdDate;

  @UpdateTimestamp
  private Timestamp lastModifiedDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = cleanAndRemoveSpaces(number);
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = cleanAndRemoveSpaces(unit);
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = cleanAndRemoveSpaces(street);
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = clean(city).replaceAll("\\s+", " ");
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = cleanAndRemoveSpaces(state);
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = cleanAndRemoveSpaces(country);
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = cleanAndRemoveSpaces(zip);
  }

  private String cleanAndRemoveSpaces(String s) {
    return clean(s).replaceAll("\\s", "");
  }

  private String clean(String s) {
    return s.trim().toUpperCase();
  }

  public String getSeedForSha1() {
    return city + "|" + country + "|" + number + "|" + state + "|" + street + "|" + unit + "|" + zip;
  }
}
