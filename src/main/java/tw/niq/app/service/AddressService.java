package tw.niq.app.service;

import java.util.List;

import tw.niq.app.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);
	
}
