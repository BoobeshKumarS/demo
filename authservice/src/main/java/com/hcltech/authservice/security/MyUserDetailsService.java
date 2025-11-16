package com.hcltech.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hcltech.authservice.entity.User;
import com.hcltech.authservice.exception.UserNotFoundException;
import com.hcltech.authservice.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			return new org.springframework.security.core.userdetails.User(user.getUsername(),  user.getPassword(),
					Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
		}
		throw new UserNotFoundException("User not found", "email", email);
	}
}