package tw.niq.app;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tw.niq.app.entity.AuthorityEntity;
import tw.niq.app.entity.RoleEntity;
import tw.niq.app.entity.UserEntity;
import tw.niq.app.repository.AuthorityRepository;
import tw.niq.app.repository.RoleRepository;
import tw.niq.app.repository.UserRepository;
import tw.niq.app.utils.UserUtils;

@Component
public class InitialUsersSetup {
	
	private final AuthorityRepository authorityRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final UserUtils userUtils;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public InitialUsersSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository,
			UserRepository userRepository, UserUtils userUtils, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.authorityRepository = authorityRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.userUtils = userUtils;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
		RoleEntity roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
		RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		
		if (roleAdmin == null) return;
		
		UserEntity adminUser = new UserEntity();
		
		adminUser.setFirstName("Nick");
		adminUser.setLastName("Chen");
		adminUser.setEmail("test@test.com");
		adminUser.setEmailVerificationStatus(true);
		adminUser.setUserId(userUtils.generateUserId(30));
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		adminUser.setRoles(Arrays.asList(roleAdmin));
		
		userRepository.save(adminUser);
	}
	
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authority = authorityRepository.findByName(name);
		
		if (authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		
		return authority;
	}
	
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepository.findByName(name);
		
		if (role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		
		return role;
	}

}
