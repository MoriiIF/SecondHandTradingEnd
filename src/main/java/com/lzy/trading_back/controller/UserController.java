package com.lzy.trading_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzy.trading_back.entity.*;
import com.lzy.trading_back.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPurseMapper userPurseMapper;
    @Autowired
    private ShoppingRecordMapper shoppingRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCheckMapper userCheckMapper;
    @Autowired
    private UserTradingMapper userTradingMapper;

    //注册
    @PostMapping("/register")
    public String register(@RequestBody UserCheck userCheck) {
        userCheck.setAdmin(false);
        if (userCheckMapper.insert(userCheck) != 0) {
            return "注册成功！";
        } else return "注册失败！";
    }

    //登录
    @GetMapping("/login")
    public String login(String userId, String password) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return "没有这个用户！";
        }
        if (Objects.equals(user.getPassword(), password)) {
            return "登录成功！";
        } else {
            return "密码错误！";
        }
    }

    //平台所有用户
    @GetMapping("/userList")
    public List<User> userList() {
        return userMapper.selectList(null);
    }

    //用户信息
    @GetMapping("/getUserInfo")
    public User getUserInfo(String id) {
        return userMapper.selectById(id);
    }

    //更改用户信息
    @PostMapping("/changeUserInfo")
    public String changeUserInfo(@RequestBody User user) {
        if (userMapper.updateById(user) != 0) {
            return "修改成功！";
        } else {
            return "修改失败！";
        }
    }

    @GetMapping("/deleteUser")
    public String deleteUserById(String userId) {
        int i = userMapper.deleteById(userId);
        int j = deleteUserRecordByUserId(userId);
        if (i != 0) {
            if (j != 0) {
                return "成功删除用户及其购买记录";
            }
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    public int deleteUserRecordByUserId(String userId) {
        QueryWrapper<ShoppingRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        int i = shoppingRecordMapper.delete(queryWrapper);
        if (i != 0) {
            return i;
        } else return 0;
    }

    @GetMapping("/purchaseProduct")
    public String purchaseProduct(String userId, String sku) {
        ShoppingRecord shoppingRecord = new ShoppingRecord();
        shoppingRecord.setOrderId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        shoppingRecord.setId(userId);
        shoppingRecord.setSku(sku);
        shoppingRecord.setStatus(1);
        if (shoppingRecordMapper.insert(shoppingRecord) != 0) {
            return shoppingRecord.getOrderId();
        } else return "购买失败！";
    }

    @GetMapping("/setPurchaseStatus")
    public String setPurchaseStatus(String userId, String sku, int status) {
        QueryWrapper<ShoppingRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        queryWrapper.eq("sku", sku);
        ShoppingRecord shoppingRecord = shoppingRecordMapper.selectOne(queryWrapper);
        shoppingRecord.setStatus(status);
        if (shoppingRecordMapper.updateById(shoppingRecord) != 0) {
            return "更改交易状态成功！";
        } else {
            return "更改交易状态失败！";
        }
    }
    //1买家已下单 2卖家已发货 3交易已完成 4买家退货中 5完成退货
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestBody ShoppingRecord shoppingRecord) {
        shoppingRecord.setOrderId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        if (shoppingRecordMapper.insert(shoppingRecord) != 0) {
            return "下单成功";
        } return "下单失败";
    }

    @GetMapping("/setHistoryStatus")
    public String setHistoryStatus(String userId, String sku, int status) {
        QueryWrapper<ShoppingRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId).eq("sku", sku);
        List<ShoppingRecord> shoppingRecord = shoppingRecordMapper.selectList(wrapper);
        shoppingRecord.forEach(item -> {
            item.setStatus(status);
            shoppingRecordMapper.updateById(item);
        });
        return "更改交易状态成功！";
    }

    @GetMapping("/purchaseHistory")
    public List<ShoppingRecord> getPurchaseHistory(String userId) {
        QueryWrapper<ShoppingRecord> query = new QueryWrapper<>();
        query.eq("id", userId);
        return shoppingRecordMapper.selectList(query);
    }

    @GetMapping("/checkBalance")
    public UserPurse checkBalance(String userId) {
        return userPurseMapper.selectById(userId);
    }

    @GetMapping("/rechargeBalance")
    public String rechargeBalance(String userId, Double money) {
        UserPurse userPurse = userPurseMapper.selectById(userId);
        userPurse.setBalance(userPurse.getBalance() + money);
        if (userPurseMapper.updateById(userPurse) != 0) {
            addTradeRecord(userId, money, 0);
            return "充值成功！";
        } else {
            return "充值失败！";
        }
    }

    @GetMapping("/rechargePoint")
    public String rechargePoint(String userId, Double point) {
        UserPurse userPurse = userPurseMapper.selectById(userId);
        userPurse.setIntegral(userPurse.getIntegral() + point);
        if (userPurseMapper.updateById(userPurse) != 0) {
            addTradeRecord(userId, 0, point);
            return "积分成功！";
        } else {
            return "积分失败！";
        }
    }

    @GetMapping("/payment")
    public String payment(String userId, double money) {
        UserPurse userPurse = userPurseMapper.selectById(userId);
        int change = 0;
        double cost = 0;
        change = (int) (userPurse.getIntegral() / 100);
        cost = money - change;
        if (userPurse.getBalance() - cost < 0) {
            return "支付失败";
        }
        System.out.println();
        userPurse.setBalance(userPurse.getBalance() - cost);
        userPurse.setIntegral(userPurse.getIntegral() - (change * 100) + cost);
        System.out.println(userPurse);
        addTradeRecord(userId, -money, -(change * 100));

        if (userPurseMapper.updateById(userPurse) != 0) {
            return "支付成功！";
        } else {
            return "支付失败！";
        }
    }

    @GetMapping("/getTradeRecord")
    public List<UserTradingRecord> getTradeRecord(String userId) {
        return getTradeRecorde(userId);
    }

    public List<UserTradingRecord> getTradeRecorde(String userId) {
        QueryWrapper<UserTradingRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userTradingMapper.selectList(queryWrapper);
    }
    public String addTradeRecord(String userId, double money, double point) {
        UserTradingRecord userTradingRecord = new UserTradingRecord();
        userTradingRecord.setTradeId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        userTradingRecord.setId(userId);
        userTradingRecord.setMoneyTrade(money);
        userTradingRecord.setIntegralTrade(point);
        if (userTradingMapper.insert(userTradingRecord) != 0) {
            return "交易成功！";
        } else {
            return "交易失败！";
        }
    }
}
