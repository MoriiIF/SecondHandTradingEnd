package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods")
public class Goods {
    @TableId
    private String sku;
    private String img;
    private String name;
    private double price;
    private String category;
    private String shop;
    private String description;
    private int stock;
    private int sales;
    private double score;
    private int status;
}
