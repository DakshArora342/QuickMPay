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
public class OrderRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String tradeNo;
	private String status;
	private double pendingPoints;
	private double buyCredits;
	private double inr;
	private String tradeTime;
	private String utr;
	private String orderType;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] transferVoucher;
	@Transient
	private String encodedTransferVoucher;
}
