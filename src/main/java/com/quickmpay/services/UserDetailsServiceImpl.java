package com.quickmpay.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.quickmpay.entities.User;
import com.quickmpay.repos.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepo repo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
	  User user = null;
    Optional<User> optional = this.repo.findByEmail(username);
    if(optional.isPresent()) {
    	user=optional.get();
    	UserDetailsImpl userDetails = new UserDetailsImpl(user);
    	return  userDetails;
    }else {
    	throw new UsernameNotFoundException("User not found");
    }
    
    
  }
}
