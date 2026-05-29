package com.campus.market.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.market.common.OrderVO;
import com.campus.market.entity.*;
import com.campus.market.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Transactional
    public Order createOrder(Long buyerId, Long productId, Integer quantity, Integer usePoints) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        QueryWrapper<Wallet> walletWrapper = new QueryWrapper<>();
        walletWrapper.eq("user_id", buyerId);
        Wallet buyerWallet = walletMapper.selectOne(walletWrapper);
        if (buyerWallet == null) {
            throw new RuntimeException("钱包不存在，请先充值");
        }

        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(quantity));
        BigDecimal pointsDeduct = BigDecimal.ZERO;
        if (usePoints != null && usePoints > 0) {
            if (usePoints > buyerWallet.getPoints()) {
                throw new RuntimeException("积分不足");
            }
            pointsDeduct = new BigDecimal(usePoints).divide(new BigDecimal(100));
            if (pointsDeduct.compareTo(totalPrice) > 0) {
                pointsDeduct = totalPrice;
                usePoints = pointsDeduct.multiply(new BigDecimal(100)).intValue();
            }
        }

        BigDecimal actualPay = totalPrice.subtract(pointsDeduct);
        if (buyerWallet.getBalance().compareTo(actualPay) < 0) {
            throw new RuntimeException("余额不足，请先充值");
        }

        buyerWallet.setBalance(buyerWallet.getBalance().subtract(actualPay));
        buyerWallet.setPoints(buyerWallet.getPoints() - usePoints + totalPrice.intValue());
        walletMapper.updateById(buyerWallet);

        QueryWrapper<Wallet> sellerWalletWrapper = new QueryWrapper<>();
        sellerWalletWrapper.eq("user_id", product.getSellerId());
        Wallet sellerWallet = walletMapper.selectOne(sellerWalletWrapper);
        if (sellerWallet == null) {
            sellerWallet = new Wallet();
            sellerWallet.setUserId(product.getSellerId());
            sellerWallet.setBalance(BigDecimal.ZERO);
            sellerWallet.setPoints(0);
            walletMapper.insert(sellerWallet);
        }
        sellerWallet.setBalance(sellerWallet.getBalance().add(actualPay));
        walletMapper.updateById(sellerWallet);

        product.setStock(product.getStock() - quantity);
        product.setSales(product.getSales() + quantity);
        productMapper.updateById(product);

        cartMapper.delete(new QueryWrapper<Cart>().eq("user_id", buyerId).eq("product_id", productId));

        Order order = new Order();
        order.setOrderNo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setBuyerId(buyerId);
        order.setSellerId(product.getSellerId());
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setUsePoints(usePoints != null ? usePoints : 0);
        order.setPointsDeduct(pointsDeduct);
        order.setStatus(0);
        orderMapper.insert(order);

        return order;
    }

    public Page<OrderVO> getBuyerOrders(Long buyerId, Integer page, Integer size) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("buyer_id", buyerId);
        wrapper.orderByDesc("create_time");
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        
        List<OrderVO> voList = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order, vo);
            Product product = productMapper.selectById(order.getProductId());
            if (product != null) {
                vo.setProductName(product.getName());
            }
            voList.add(vo);
        }
        
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public Page<OrderVO> getSellerOrders(Long sellerId, Integer page, Integer size) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id", sellerId);
        wrapper.orderByDesc("create_time");
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        
        List<OrderVO> voList = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order, vo);
            Product product = productMapper.selectById(order.getProductId());
            if (product != null) {
                vo.setProductName(product.getName());
            }
            voList.add(vo);
        }
        
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Transactional
    public boolean shipOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(1);
        orderMapper.updateById(order);
        return true;
    }

    @Transactional
    public boolean confirmReceive(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(2);
        orderMapper.updateById(order);
        return true;
    }

    @Transactional
    public boolean applyRefund(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法申请退货");
        }
        order.setStatus(3);
        orderMapper.updateById(order);
        return true;
    }

    @Transactional
    public boolean approveRefund(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 3) {
            throw new RuntimeException("订单状态不正确");
        }

        Wallet buyerWallet = walletMapper.selectOne(new QueryWrapper<Wallet>().eq("user_id", order.getBuyerId()));
        if (buyerWallet == null) {
            buyerWallet = new Wallet();
            buyerWallet.setUserId(order.getBuyerId());
            buyerWallet.setBalance(BigDecimal.ZERO);
            buyerWallet.setPoints(0);
            walletMapper.insert(buyerWallet);
        }

        BigDecimal refundAmount = order.getTotalPrice();
        buyerWallet.setBalance(buyerWallet.getBalance().add(refundAmount));
        walletMapper.updateById(buyerWallet);

        Product product = productMapper.selectById(order.getProductId());
        if (product != null) {
            product.setStock(product.getStock() + order.getQuantity());
            product.setSales(product.getSales() - order.getQuantity());
            productMapper.updateById(product);
        }

        order.setStatus(4);
        orderMapper.updateById(order);

        return true;
    }

    public boolean rejectRefund(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 3) {
            throw new RuntimeException("订单状态不正确");
        }
        order.setStatus(2);
        orderMapper.updateById(order);
        return true;
    }

    public Order getOrderById(Long orderId) {
        return orderMapper.selectById(orderId);
    }
}
