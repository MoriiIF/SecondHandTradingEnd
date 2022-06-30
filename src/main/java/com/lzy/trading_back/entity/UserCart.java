package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_cart")
public class UserCart {
    private int id;
    private String userId;
    private String goodsSku;
}
