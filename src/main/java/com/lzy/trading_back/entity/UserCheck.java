package com.lzy.trading_back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admin_check_user")
public class UserCheck {
    private String id;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private String city;
    private String bank;
    private String password;
    private int type;
    private boolean admin;
}