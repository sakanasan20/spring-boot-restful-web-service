package tw.niq.app.service;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if (userEntity == null) 
			throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
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
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if (userEntity == null) 
			throw new UsernameNotFoundException(email);
				
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

}
