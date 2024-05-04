package com.quickmpay.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.Wallet;
import com.quickmpay.repos.PercentageRepo;
import com.quickmpay.repos.RateRepo;
import com.quickmpay.repos.TipsRepo;
import com.quickmpay.repos.WalletRepo;

@Service
public class AdminServiceImpl implements AdminService
{
	
	RateRepo rateRepo;
	WalletRepo walletRepo;
	PercentageRepo percentageRepo;
	TipsRepo tipsRepo;
	AdminServiceImpl(RateRepo rateRepo,WalletRepo walletRepo,PercentageRepo percentageRepo,TipsRepo tipsRepo)
	{
		this.rateRepo=rateRepo;
		this.walletRepo=walletRepo;
		this.percentageRepo=percentageRepo;
		this.tipsRepo=tipsRepo;
	}

	@Override
	public Rate addRate(Rate rate) 
	{
		Rate rates;
		Optional<Rate> optional = rateRepo.findById(1);
		if(optional.isPresent()) {
			rates=optional.get();
			rates.setRateAmount(rate.getRateAmount());
			return rateRepo.save(rates);
		}
		return rateRepo.save(rate);
	}

	@Override
	public Wallet addWallet(Wallet wallet) {
		Wallet wallet2;
		Optional<Wallet> optional = walletRepo.findById(1);
		if(optional.isPresent()) {
			wallet2=optional.get();
			wallet2.setQrCode(wallet.getQrCode());
			wallet2.setWalletNo(wallet.getWalletNo());
			return walletRepo.save(wallet2);
		}
		return walletRepo.save(wallet);
	}

	@Override
	public Rate findRateById(Integer id) {
		Rate rates = null;
		Optional<Rate> optional = rateRepo.findById(1);
		if(optional.isPresent()) {
			rates=optional.get();
		}
		return rates;
	}

	@Override
	public Percentage addPercentage(Percentage percentage) {
		Percentage percentage2 = percentageRepo.findByAccountType(percentage.getAccountType());
		if(percentage2!=null) {
		percentage2.setPercentage(percentage.getPercentage());
		return percentageRepo.save(percentage2);
		}else {
			return percentageRepo.save(percentage);

		}
	}

	@Override
	public Tips addTip(Tips tips) {
		Tips tips2;
		Optional<Tips> optional = tipsRepo.findById(1);
		if(optional.isPresent()) {
			tips2=optional.get();
			tips2.setTip(tips.getTip());
			return tipsRepo.save(tips2);
		}
		return tipsRepo.save(tips);
	}
 	 
}
