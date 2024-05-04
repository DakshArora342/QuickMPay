package com.quickmpay.services;

import com.quickmpay.dtos.UserAccountDto;
import com.quickmpay.entities.Account;
import com.quickmpay.entities.Order;

public interface OrderService {

public Order findOrderById(String id); 
public Order saveOrder(Order order);
public Order findByTradeNo(String tradeNo);

}
