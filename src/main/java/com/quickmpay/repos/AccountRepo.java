package com.quickmpay.repos;

import com.quickmpay.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AccountRepo extends JpaRepository<Account,String> 
{
}
