package com.quickmpay.dtos;


import com.quickmpay.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycDetailsDto {
    private String id;
  private byte[] aadharFrontPage;
  private byte[] aadharBackPage;
  private  byte[] panCard;
  private UserDto user;

}
