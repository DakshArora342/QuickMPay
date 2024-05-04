package com.quickmpay.services;

import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.Wallet;

public interface AdminService {
	public Rate addRate(Rate rate);
	public Wallet addWallet(Wallet wallet);
	public Rate findRateById(Integer id);
	public Percentage addPercentage(Percentage percentage);
	public Tips addTip(Tips tips);
}
