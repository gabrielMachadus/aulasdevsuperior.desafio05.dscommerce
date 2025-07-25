package br.com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.devsuperior.dscommerce.dto.UserDTO;
import br.com.devsuperior.dscommerce.entities.Role;
import br.com.devsuperior.dscommerce.entities.User;
import br.com.devsuperior.dscommerce.projections.UserDetailsProjection;
import br.com.devsuperior.dscommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();
		user.setEmail(result.getFirst().getUsername());
		user.setPassword(result.getFirst().getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}
	
	protected User authenticated() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
			return repository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Email não encontrado: "+username));
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Invalid user");
		}
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User entity = authenticated();
		return new UserDTO(entity);
	}
}
