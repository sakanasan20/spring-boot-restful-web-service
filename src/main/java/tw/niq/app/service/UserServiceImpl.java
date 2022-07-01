package tw.niq.app.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tw.niq.app.dto.UserDto;
import tw.niq.app.entity.UserEntity;
import tw.niq.app.repository.UserRepository;
import tw.niq.app.utils.UserUtils;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserUtils userUtils;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository, UserUtils userUtils,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.userUtils = userUtils;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto user) {
		
		if (userRepository.findUserByEmail(user.getEmail()) != null) 
			throw new RuntimeException("Record alread exist");
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = userUtils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userRepository .save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

}
