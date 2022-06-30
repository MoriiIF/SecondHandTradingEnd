package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_trading_record")
public class UserTradingRecord {
    @TableId
    private String tradeId;
    private String id;
    private double moneyTrade;
    private double integralTrade;
}
