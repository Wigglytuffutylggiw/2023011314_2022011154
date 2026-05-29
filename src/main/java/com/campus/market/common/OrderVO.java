package com.campus.market.common;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private Long buyerId;
    private Long sellerId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer usePoints;
    private BigDecimal pointsDeduct;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
