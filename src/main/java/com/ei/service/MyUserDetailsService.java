package com.ei.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Hardcoded user details with roles
		if (username.equals("admin")) {
			return User.withUsername("admin").password("admin@123").roles("ADMIN", "USER").build();
		} else if (username.equals("user")) {
			return User.withUsername("user").password("user@123").roles("USER").build();
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}