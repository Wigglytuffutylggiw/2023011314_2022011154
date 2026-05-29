package com.campus.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.market.entity.Wallet;
import com.campus.market.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {
    @Autowired
    private WalletMapper walletMapper;

    public Wallet getByUserId(Long userId) {
        QueryWrapper<Wallet> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return walletMapper.selectOne(wrapper);
    }

    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setPoints(0);
        walletMapper.insert(wallet);
        return wallet;
    }

    public Wallet recharge(Long userId, BigDecimal amount) {
        Wallet wallet = getByUserId(userId);
        if (wallet == null) {
            wallet = createWallet(userId);
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        walletMapper.updateById(wallet);
        return wallet;
    }

    public Wallet deduct(Long userId, BigDecimal amount) {
        Wallet wallet = getByUserId(userId);
        if (wallet == null || wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletMapper.updateById(wallet);
        return wallet;
    }

    public Wallet refund(Long userId, BigDecimal amount) {
        return recharge(userId, amount);
    }
}
