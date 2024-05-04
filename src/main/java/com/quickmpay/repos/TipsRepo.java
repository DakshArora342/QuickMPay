package com.quickmpay.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipsRepo extends JpaRepository<Tips, Integer> {
	 
}