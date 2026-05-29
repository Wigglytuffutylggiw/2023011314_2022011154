package com.campus.market.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.common.OrderVO;
import com.campus.market.common.Result;
import com.campus.market.entity.Order;
import com.campus.market.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Map<String, Object> params) {
        try {
            Long buyerId = Long.valueOf(params.get("buyerId").toString());
            Long productId = Long.valueOf(params.get("productId").toString());
            Integer quantity = Integer.valueOf(params.getOrDefault("quantity", 1).toString());
            Integer usePoints = Integer.valueOf(params.getOrDefault("usePoints", 0).toString());
            return Result.success(orderService.createOrder(buyerId, productId, quantity, usePoints));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/buyer/{buyerId}")
    public Result<Page<OrderVO>> getBuyerOrders(
            @PathVariable Long buyerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(orderService.getBuyerOrders(buyerId, page, size));
    }

    @GetMapping("/seller/{sellerId}")
    public Result<Page<OrderVO>> getSellerOrders(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(orderService.getSellerOrders(sellerId, page, size));
    }

    @PostMapping("/{id}/ship")
    public Result<Boolean> shipOrder(@PathVariable Long id) {
        try {
            return Result.success(orderService.shipOrder(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/receive")
    public Result<Boolean> confirmReceive(@PathVariable Long id) {
        try {
            return Result.success(orderService.confirmReceive(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund/apply")
    public Result<Boolean> applyRefund(@PathVariable Long id) {
        try {
            return Result.success(orderService.applyRefund(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund/approve")
    public Result<Boolean> approveRefund(@PathVariable Long id) {
        try {
            return Result.success(orderService.approveRefund(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund/reject")
    public Result<Boolean> rejectRefund(@PathVariable Long id) {
        try {
            return Result.success(orderService.rejectRefund(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }
}
