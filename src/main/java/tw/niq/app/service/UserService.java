package tw.niq.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import tw.niq.app.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto user);

	UserDto getUser(String email);
	
}
