package com.quickmpay.dtos;

import java.util.List;
import java.util.Set;

import com.quickmpay.entities.Account;
import com.quickmpay.entities.KycDetails;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;
import com.quickmpay.entities.Role;
import com.quickmpay.services.AccountDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private String id;
	@NotEmpty(message = "Name is required")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
	private String name;

	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotEmpty(message = "Mobile number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number")
	private String mobile;

	@NotEmpty(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character")
	private String password;
    private UserAccountDto accounts;
    
    private List<RiskyAccountDto> riskyAccounts;
    
	private List<Order> orders;
	private List<OrderRecord> orderRecords;

	private Set<RoleDto> roles;
	private String status;
	private String otp;
    private KycDetailsDto kycDetails ;


}
