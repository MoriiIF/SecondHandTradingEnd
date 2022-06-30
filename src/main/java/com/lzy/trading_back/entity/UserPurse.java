package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_purse")
public class UserPurse {
    @TableId
    private String id;
    private double balance;
    private double integral;
}
