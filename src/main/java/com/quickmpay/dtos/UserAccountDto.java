package com.quickmpay.dtos;


import com.quickmpay.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {
	private String id;

	@NotBlank(message = "Account number is required")
    private String accountNo;

    @NotBlank(message = "Account holder name is required")
    @Size(min = 2, max = 50, message = "Account holder name must be between 2 and 50 characters")
    private String accountHolderName;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    @NotBlank(message = "IFSC code is required")
    private String ifscCode;
    
    private  String upiId;
	private byte[] qrCode;
	private  String accountType="Safe/Digital";
    private UserDto user;

}
