package com.quickmpay.entities;

import java.util.Base64;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String orderId;
	private String orderType;
	private double pendingPoints;
	private double amount;
	private String tradeNo;
	private String buyCredits;
	private String rupeeDollarRate;
	private double getPoints;
	private String tradeTime;
	private String  walletAddress;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] paymentQr;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] transferVoucher;
	private double topupFees;
	private String status;
	@Transient
	private String encodedTransferVoucher;
}
