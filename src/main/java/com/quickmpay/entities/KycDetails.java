package com.quickmpay.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KycDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
    private byte[] aadharFrontPage;
    @Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
    private byte[] aadharBackPage;
    @Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
    private  byte[] panCard;
    
    @OneToOne(fetch = FetchType.LAZY , optional = false)
	 @JoinColumn(name="userId", nullable = false)
	    private User user;
	
}
