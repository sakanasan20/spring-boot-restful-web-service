package tw.niq.app.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.niq.app.dto.AddressDto;
import tw.niq.app.dto.UserDto;
import tw.niq.app.error.ErrorMessages;
import tw.niq.app.exception.UserServiceException;
import tw.niq.app.model.AddressRest;
import tw.niq.app.model.OperationStatusModel;
import tw.niq.app.model.UserDetailsRequestModel;
import tw.niq.app.model.UserRest;
import tw.niq.app.operation.RequestOperaionName;
import tw.niq.app.operation.RequestOperationStatus;
import tw.niq.app.service.AddressService;
import tw.niq.app.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	
	private final UserService userService;
	private final AddressService addressService;
	
	public UserController(UserService userService, AddressService addressService) {
		this.userService = userService;
		this.addressService = addressService;
	}

	@GetMapping(
			path = "/{id}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String id) {

		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		
		UserRest returnValue = new UserRest();
		
		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		ModelMapper modelMpper = new ModelMapper();
		UserDto userDto = modelMpper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMpper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(
			path = "/{id}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(
			@PathVariable String id, 
			@RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(
			path = "/{id}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		userService.deleteUser(id);
		
		returnValue.setOperationName(RequestOperaionName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue ;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(
			@RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "limit", defaultValue = "25") int limit) {

		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto user : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(user, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	@GetMapping(
			path = "/{id}/addresses", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<AddressRest> getUserAddresses(@PathVariable String id) {
		
		List<AddressDto> addressesDto = addressService.getAddresses(id);

		List<AddressRest> returnValue = new ArrayList<>();
		
		if (addressesDto != null && !addressesDto.isEmpty()) {
			ModelMapper modelMpper = new ModelMapper();
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = modelMpper.map(addressesDto, listType);
		}

		return returnValue;
	}
	
}
