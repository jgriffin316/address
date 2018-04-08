package com.metacore.address.address;

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
    this.number = number.toUpperCase();
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit.toUpperCase();
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street.toUpperCase();
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city.toUpperCase();
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state.toUpperCase();
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country.toUpperCase();
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip.replaceAll("\\s", "");
  }
}
