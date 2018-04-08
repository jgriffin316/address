package com.metacore.address.address.resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddressResourceRequest {
	@NotBlank
	@Pattern(regexp="[1-9][0-9a-dA-D]*")
	public String number;
	
	public String unit;
	
	@NotBlank
	public String street;
	
	@NotBlank
	public String city;
	
	@NotBlank
	public String zip;
	
	@NotBlank
	public String state;
	
	@NotNull
	@Size(min=2, max=2)
	public String country;
}
