package com.quickmpay.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickmpay.entities.Rate;
import com.quickmpay.entities.User;
import com.quickmpay.entities.Wallet;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Integer> {
	 
}
