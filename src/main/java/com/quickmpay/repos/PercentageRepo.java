package com.quickmpay.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PercentageRepo extends JpaRepository<Percentage, String> {
	 Percentage findByAccountType(String accountType);
}
