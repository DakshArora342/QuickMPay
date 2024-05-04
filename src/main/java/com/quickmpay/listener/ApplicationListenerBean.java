package com.quickmpay.listener;

import org.springframework.stereotype.Component;

import com.quickmpay.dtos.UserDto;
import com.quickmpay.services.UserService;

import jakarta.annotation.PostConstruct;
 
@Component
public class ApplicationListenerBean   
{
	private UserService userService;
	
	ApplicationListenerBean(UserService userService)
	{
	this.userService=userService;	
	}
	
	 @PostConstruct
	    public void init() {
		 
		 UserDto dto=new UserDto();
		 dto.setEmail("simplepay0000@gmail.com");
		 dto.setName("admin");
		 dto.setMobile("1111111111");
		 dto.setPassword("admin@123");
		 userService.addAdmin(dto);
		 
	    }

}