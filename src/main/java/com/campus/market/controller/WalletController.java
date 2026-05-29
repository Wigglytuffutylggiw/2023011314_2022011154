package com.campus.market.controller;

import com.campus.market.common.Result;
import com.campus.market.entity.Wallet;
import com.campus.market.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public Result<Wallet> getByUserId(@PathVariable Long userId) {
        return Result.success(walletService.getByUserId(userId));
    }

    @PostMapping("/recharge")
    public Result<Wallet> recharge(@RequestBody WalletRechargeRequest request) {
        try {
            return Result.success(walletService.recharge(request.getUserId(), request.getAmount()));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

class WalletRechargeRequest {
    private Long userId;
    private BigDecimal amount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
