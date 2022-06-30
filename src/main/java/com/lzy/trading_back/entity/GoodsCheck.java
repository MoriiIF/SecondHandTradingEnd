package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admin_check_goods")
public class GoodsCheck {
    @TableId
    private String goodsId;
    private String name;
    private double price;
    private String category;
    private String img;
    private String description;
    private int stock;
    private String shop;
}
