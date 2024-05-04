package com.quickmpay.entities;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String accountNo;
	private String accountHolderName;
	private String bankName;
	private String ifscCode;
	private  String upiId;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] qrCode;
	
	 @OneToOne(fetch = FetchType.LAZY , optional = false)
	 @JoinColumn(name="userId", nullable = false)
	    private User user;
	
	
	private  String accountType;
	 
//	@OneToOne(cascade = CascadeType.ALL)
//	    private User user;
}
