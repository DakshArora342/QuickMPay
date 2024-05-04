package com.quickmpay.repos;

import com.quickmpay.entities.Account;
import com.quickmpay.entities.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrderRepo extends JpaRepository<Order,String> 
{
	List<Order> findByOrderType(String orderType);
	Order findByTradeNo(String tradeNo);
}

