package com.campus.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.market.entity.User;
import com.campus.market.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (user.getRole() == 1 && (user.getShopName() == null || user.getShopName().trim().isEmpty())) {
            throw new RuntimeException("商家注册必须填写店铺名称");
        }
        
        user.setStatus(0);
        user.setBusinessLevel(1);
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("账户未审核或已封禁");
        }
        return user;
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
