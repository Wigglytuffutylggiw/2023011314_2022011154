package com.campus.market.controller;

import com.campus.market.common.Result;
import com.campus.market.entity.User;
import com.campus.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            User registered = userService.register(user);
            return Result.success(registered);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        try {
            User loginUser = userService.login(user.getUsername(), user.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser);
            data.put("token", "token_" + loginUser.getId());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
}
