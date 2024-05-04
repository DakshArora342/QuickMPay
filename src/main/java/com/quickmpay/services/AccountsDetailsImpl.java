package com.quickmpay.services;

import com.quickmpay.dtos.UserAccountDto;
import com.quickmpay.entities.Account;
import com.quickmpay.repos.AccountRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class AccountsDetailsImpl implements AccountDetails{


   private AccountRepo repo;
   private ModelMapper mapper;


  public AccountsDetailsImpl(AccountRepo repo, ModelMapper mapper) {
    this.repo = repo;
    this.mapper = mapper;
  }

  @Override
  public UserAccountDto addAccoundDetails(UserAccountDto userAccountDto) {
    Account map = mapper.map(userAccountDto, Account.class);
    Account save = repo.save(map);
 return    mapper.map(save,UserAccountDto.class);

  }

}
