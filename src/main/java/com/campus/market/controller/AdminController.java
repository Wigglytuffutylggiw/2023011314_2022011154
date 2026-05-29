package com.campus.market.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.common.Result;
import com.campus.market.entity.User;
import com.campus.market.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/users/pending")
    public Result<Page<User>> getPendingUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.getPendingUsers(page, size));
    }

    @PostMapping("/users/{id}/approve")
    public Result<Boolean> approveUser(@PathVariable Long id) {
        try {
            return Result.success(adminService.approveUser(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/users/{id}/reject")
    public Result<Boolean> rejectUser(@PathVariable Long id) {
        try {
            return Result.success(adminService.rejectUser(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/users")
    public Result<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminService.getAllUsers(page, size));
    }
}
