package com.quickmpay.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "wallet")
public class Wallet {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int walletId;
private String walletNo;
@Lob @Basic(fetch = FetchType.LAZY)
@Column(length=100000)
private byte[] qrCode;



	
}
