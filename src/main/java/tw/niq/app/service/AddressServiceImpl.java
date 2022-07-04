package tw.niq.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import tw.niq.app.dto.AddressDto;
import tw.niq.app.entity.AddressEntity;
import tw.niq.app.entity.UserEntity;
import tw.niq.app.repository.AddressRepository;
import tw.niq.app.repository.UserRepository;

@Service
public class AddressServiceImpl implements AddressService {
	
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;

	public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
	}

	@Override
	public List<AddressDto> getAddresses(String userId) {
		
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		
		AddressDto returnValue = null;
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if (addressEntity != null) {
			ModelMapper modelMapper = new ModelMapper();
			
			returnValue = modelMapper.map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

}
