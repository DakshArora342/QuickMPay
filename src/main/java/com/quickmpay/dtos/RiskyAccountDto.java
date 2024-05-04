package com.quickmpay.dtos;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RiskyAccountDto {

	
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
	
	
	
	private  String accountType="Risky";
	 

}
