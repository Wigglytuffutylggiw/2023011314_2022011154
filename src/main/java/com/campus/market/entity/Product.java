package com.campus.market.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    @TableField("product_condition")
    private Integer productCondition;
    private String images;
    private String sellerShopName;
    private Integer status;
    private Integer sales;
    private Double rating;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
