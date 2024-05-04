package com.quickmpay.repos;

import com.quickmpay.entities.Account;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrderRecordRepo extends JpaRepository<OrderRecord,String> 
{
	List<OrderRecord> findByOrderType(String orderType);
//	Order findByTradeNo(String tradeNo);
}

