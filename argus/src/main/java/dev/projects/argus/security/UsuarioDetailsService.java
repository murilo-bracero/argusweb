package dev.projects.argus.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import dev.projects.argus.repository.UserRepository;

@Repository
@Transactional
public class UsuarioDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository users;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		dev.projects.argus.model.User user = users.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Dados Incorretos.");
		}
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, user.getAuthorities());
	}

}
