package com.eventmanagement.service;

import java.util.List;

import com.eventmanagement.entity.Address;
import com.eventmanagement.entity.User;

public interface AddressService {
	
	Address addAddress(Address address);
	
	Address updateAddress(Address address);
	
	Address getAddressById(int addressId);

}
