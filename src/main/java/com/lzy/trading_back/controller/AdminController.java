package com.lzy.trading_back.controller;


import com.lzy.trading_back.entity.*;
import com.lzy.trading_back.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserCheckMapper userCheckMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsCheckMapper goodsCheckMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserPurseMapper userPurseMapper;


    @RequestMapping("/list")
    public List<UserCheck> list() {
        return userCheckMapper.selectList(null);
    }
    @RequestMapping("/userAudit")
    public String userAudit(String id, boolean result) {
        if (result) {
            UserCheck userCheck = userCheckMapper.selectById(id);
            User user = new User();
            user.setId(userCheck.getId());
            user.setName(userCheck.getName());
            user.setPhone(userCheck.getPhone());
            user.setEmail(userCheck.getEmail());
            user.setCity(userCheck.getCity());
            user.setSex(userCheck.getSex());
            user.setBank(userCheck.getBank());
            user.setType(userCheck.getType());
            user.setAdmin(userCheck.isAdmin());
            user.setPassword(userCheck.getPassword());
            userMapper.insert(user);
            addUserPurse(userCheck.getId());
            userCheckMapper.deleteById(id);
            return "审核通过！";
        } else {
            userCheckMapper.deleteById(id);
            return "审核未通过！";
        }
    }
    @GetMapping("/goodsList")
    public List<GoodsCheck> goodsList() {
        return goodsCheckMapper.selectList(null);
    }
    @GetMapping("/commodityAudit")
    public String commodityAudit(String id, boolean result) {
        if (result) {
            GoodsCheck goodsCheck = goodsCheckMapper.selectById(id);
            Goods goods = new Goods();
            goods.setSku(getSku());
            goods.setImg(goodsCheck.getImg());
            goods.setName(goodsCheck.getName());
            goods.setCategory(goodsCheck.getCategory());
            goods.setPrice(goodsCheck.getPrice());
            goods.setDescription(goodsCheck.getDescription());
            goods.setStock(goodsCheck.getStock());
            goods.setStatus(1);
            goods.setShop(goodsCheck.getShop());
            goodsMapper.insert(goods);
            goodsCheckMapper.deleteById(id);
            return "审核通过！";
        } else {
            goodsCheckMapper.deleteById(id);
            return "审核未通过！";
        }
    }
    public String getSku() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    public int addUserPurse(String userId) {
        UserPurse userPurse = new UserPurse();
        userPurse.setId(userId);
        int i = userPurseMapper.insert(userPurse);
        if (i != 0) {
            return i;
        } else return 0;
    }
}
