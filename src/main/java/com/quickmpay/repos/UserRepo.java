package com.quickmpay.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickmpay.entities.User;

import java.util.List;
import java.util.Optional;
import com.quickmpay.entities.Order;


@Repository
public interface UserRepo extends JpaRepository<User, String> {
	public Optional<User> findByEmail(String email);
	public User findByEmailAndPassword(String email, String password);
	public User findByEmailAndOtp(String email, String otp);
	public List<User> findByOrders(List<Order> orders);
	public List<User> findByEmailOrMobile(String email, String mobile);

}
