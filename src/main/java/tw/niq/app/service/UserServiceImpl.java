package tw.niq.app.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import tw.niq.app.UserRepository;
import tw.niq.app.dto.UserDto;
import tw.niq.app.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto createUser(UserDto user) {
		
		if (userRepository.findUserByEmail(user.getEmail()) != null) 
			throw new RuntimeException("Record alread exist");
		
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setUserId("testUserId");
		
		userEntity.setEncryptedPassword("testPassword");
		
		UserEntity storedUserDetails = userRepository .save(userEntity);
		
		UserDto returnValue = new UserDto();
		
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

}
