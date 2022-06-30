package com.lzy.trading_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzy.trading_back.entity.UserCart;
import com.lzy.trading_back.entity.ProductInCart;
import com.lzy.trading_back.entity.Goods;
import com.lzy.trading_back.entity.Order;
import com.lzy.trading_back.mapper.UserCartMapper;
import com.lzy.trading_back.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class UserCartController {
    @Autowired
    private UserCartMapper userCartMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @GetMapping("/addCart")
    public String addCart(String userId, String sku) {
        UserCart userCart = new UserCart();
        userCart.setUserId(userId);
        userCart.setGoodsSku(sku);
        if (userCartMapper.insert(userCart) != 0) {
            return "添加购物车成功！";
        } else {
            return "添加购物车失败";
        }
    }
    @GetMapping("/addCartByName")
    public String addCartByName(String userId, String name) {
        String sku = getSku(name);
        return addCart(userId, sku);
    }
    public String getSku(String name) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return goodsMapper.selectOne(queryWrapper).getSku();
    }
    @GetMapping("/deleteCart")
    public String deleteCart(String userId, String sku) {
        QueryWrapper<UserCart> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.eq("goods_sku", sku);
        System.out.println(userId + "and " + sku);
        if (userCartMapper.delete(query) != 0) {
            return "删除成功！";
        } else return "删除失败！";
    }
    @GetMapping("/deleteCartByName")
    public String deleteCartByName(String userId, String name) {
        String sku = getSku(name);
        System.out.println(sku);
        return deleteCart(userId, sku);
    }

    @GetMapping("/getCartList")
    public Order getCartList(String userId) {
        QueryWrapper<UserCart> query =  new QueryWrapper<>();
        query.eq("user_id", userId);
        List<UserCart> userCartList = userCartMapper.selectList(query);
        ArrayList<ProductInCart> arrayList = new ArrayList<>();
        final double[] sum = {0};
        userCartList.forEach(item -> {
            Goods goods = goodsMapper.selectById(item.getGoodsSku());
            ProductInCart productInCart = new ProductInCart();
            productInCart.setId(goods.getSku());
            productInCart.setProductImg(goods.getImg());
            productInCart.setShop(goods.getShop());
            productInCart.setName(goods.getName());
            productInCart.setPrice("" + goods.getPrice());
            productInCart.setCount("1");
            productInCart.setSales("1000");
            productInCart.setDescription(goods.getDescription());
            productInCart.setTradingStatus("");
            productInCart.setCost("");
            arrayList.add(productInCart);
            sum[0] += (Double.parseDouble(productInCart.getPrice()) * Double.parseDouble(productInCart.getCount()));
        });
        Order order = new Order();
        order.setProductInCarts(arrayList);
        order.setSum(sum[0]);
        return order;
    }
}
