package com.campus.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.entity.Product;
import com.campus.market.entity.User;
import com.campus.market.mapper.ProductMapper;
import com.campus.market.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;

    public Page<Product> search(String keyword, Integer page, Integer size) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword);
        }
        wrapper.orderByDesc("create_time");
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Page<Product> searchBySeller(Long sellerId, Integer page, Integer size) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id", sellerId);
        wrapper.orderByDesc("create_time");
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    public Product publish(Product product) {
        User seller = userMapper.selectById(product.getSellerId());
        if (seller != null && seller.getShopName() != null && !seller.getShopName().isEmpty()) {
            product.setSellerShopName(seller.getShopName());
        }
        product.setStatus(0);
        product.setSales(0);
        product.setRating(5.0);
        productMapper.insert(product);
        return product;
    }

    public Page<Product> getPendingProducts(Integer page, Integer size) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.orderByDesc("create_time");
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public boolean approveProduct(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        product.setStatus(1);
        productMapper.updateById(product);
        return true;
    }

    public boolean rejectProduct(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        productMapper.deleteById(productId);
        return true;
    }

    public boolean offShelf(Long productId, Long sellerId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!product.getSellerId().equals(sellerId)) {
            throw new RuntimeException("您没有权限操作此商品");
        }
        product.setStatus(2);
        productMapper.updateById(product);
        return true;
    }
}
