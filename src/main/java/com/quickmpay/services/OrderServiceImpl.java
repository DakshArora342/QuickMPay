package com.quickmpay.services;

import com.quickmpay.dtos.UserAccountDto;
import com.quickmpay.entities.Account;
import com.quickmpay.entities.Order;
import com.quickmpay.repos.AccountRepo;
import com.quickmpay.repos.OrderRepo;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService{


  OrderRepo orderRepo;

  public OrderServiceImpl(OrderRepo orderRepo) {
    this.orderRepo=orderRepo;
  }

@Override
public Order findOrderById(String id) {
	Optional<Order> optional = orderRepo.findById(id);
	if(optional.isPresent())
	return optional.get();
	return null;
}

@Override
public Order saveOrder(Order order) {
	return orderRepo.save(order);
}

@Override
public Order findByTradeNo(String tradeNo) {
	// TODO Auto-generated method stub
	return orderRepo.findByTradeNo(tradeNo);
}

  

}
