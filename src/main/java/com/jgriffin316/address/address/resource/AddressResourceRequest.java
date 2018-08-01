package com.jgriffin316.address.address.resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddressResourceRequest {
  @NotBlank(message = "Number cannot be empty and may contain numbers and the letters A-D as a postfix")
  @Pattern(regexp = "[1-9][0-9a-dA-D]*")
  public String number;

  public String unit;

  @NotBlank(message = "Street cannot be empty")
  public String street;

  @NotBlank(message = "City cannot be empty")
  public String city;

  @NotBlank(message = "Zip code cannot be empty")
  public String zip;

  @NotBlank()
  public String state;

  @NotNull(message = "Country cannot be empty must be a valid ISO 3166-1 Alpha-2 coundtry code")
  @Size(min = 2, max = 2)
  public String country;
}
