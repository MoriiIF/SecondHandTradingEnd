package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("shopping_record")
public class ShoppingRecord {
    @TableId
    private String orderId;
    private String id;
    private String sku;
    private String img;
    private String shop;
    private double price;
    private int count;
    private double payment;
    private int status;
}
