package com.quickmpay.services;

import com.quickmpay.dtos.LoginDto;

import com.quickmpay.dtos.UserDto;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;
import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.User;

import java.util.*;
import com.quickmpay.entities.Wallet;


public interface UserService {
	public List<UserDto> getAllUsers(int pageNo);
	public List<UserDto> getAllUsers();
	public List<Order> getAllOrders();
	public UserDto addUser(UserDto userDto);
	public UserDto addAdmin(UserDto userDto);
	public void deleteUserById(String id);
	public UserDto updateUser(UserDto userDto);
	public UserDto updatePass(UserDto userDto);
	public String findUser(LoginDto loginDto);
	public String verify(String email,String otp);
	public LoginDto resendOtp(LoginDto loginDto);
	public String forgetPass(String email);
	public UserDto findUserByEmail(String email);
	public UserDto findUserById(String id);
	public Rate findRateById(int id);
	public Percentage findPercentByAccountType(String type);
	public Wallet findWalletById(int id);
	public Tips findTipsById(int id);
	public List<Order> findByOrderType(String orderType);
	public List<User> findByOrders(List<Order> orders);
	public User findByOrder(Order orders);
	public List<OrderRecord> findOrderRecordByOrderType(String orderType);
	public List<User> findUserByEmailOrMobile(String email, String mobile);

}
