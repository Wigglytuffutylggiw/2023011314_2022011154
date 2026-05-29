package com.campus.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.entity.User;
import com.campus.market.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletService walletService;

    public Page<User> getPendingUsers(Integer page, Integer size) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.orderByDesc("create_time");
        return userMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public boolean approveUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(1);
        userMapper.updateById(user);
        if (user.getRole() == 0) {
            walletService.createWallet(userId);
        }
        return true;
    }

    public boolean rejectUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(userId);
        return true;
    }

    public Page<User> getAllUsers(Integer page, Integer size) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return userMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
