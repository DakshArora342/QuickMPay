package com.quickmpay.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.quickmpay.entities.Role;
import com.quickmpay.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;
  User user;
  UserDetailsImpl(User user) {
   this.user=user;
  }

  @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles=user.getRoles();
		List <SimpleGrantedAuthority> authorities = new ArrayList();
		
		for(Role role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}


  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true;
  }

}
