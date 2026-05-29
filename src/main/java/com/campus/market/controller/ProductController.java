package com.campus.market.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.common.Result;
import com.campus.market.entity.Product;
import com.campus.market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public Result<Page<Product>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(productService.search(keyword, page, size));
    }

    @GetMapping("/seller/{sellerId}")
    public Result<Page<Product>> getSellerProducts(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(productService.searchBySeller(sellerId, page, size));
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @PostMapping("/publish")
    public Result<Product> publish(@RequestBody Product product) {
        try {
            return Result.success(productService.publish(product));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pending")
    public Result<Page<Product>> getPendingProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(productService.getPendingProducts(page, size));
    }

    @PostMapping("/{id}/approve")
    public Result<Boolean> approveProduct(@PathVariable Long id) {
        try {
            return Result.success(productService.approveProduct(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    public Result<Boolean> rejectProduct(@PathVariable Long id) {
        try {
            return Result.success(productService.rejectProduct(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/offshelf")
    public Result<Boolean> offShelf(@PathVariable Long id, @RequestBody java.util.Map<String, Object> params) {
        try {
            Long sellerId = Long.valueOf(params.get("sellerId").toString());
            return Result.success(productService.offShelf(id, sellerId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
