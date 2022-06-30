package com.lzy.trading_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzy.trading_back.entity.Goods;
import com.lzy.trading_back.entity.GoodsCheck;
import com.lzy.trading_back.entity.ShoppingRecord;
import com.lzy.trading_back.mapper.GoodsMapper;
import com.lzy.trading_back.mapper.GoodsCheckMapper;
import com.lzy.trading_back.mapper.ShoppingRecordMapper;
import com.lzy.trading_back.util.CommodityComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsCheckMapper goodsCheckMapper;

    @Autowired
    private ShoppingRecordMapper shoppingRecordMapper;

    @GetMapping("/goodsListAll")
    public List<Goods> listAll() {
        return goodsMapper.selectList(null);
    }

    @PostMapping("/addGoods")
    public String addGoods(@RequestBody Goods goods) {
        GoodsCheck goodsCheck = new GoodsCheck();
        goodsCheck.setName(goods.getName());
        goodsCheck.setPrice(goods.getPrice());
        goodsCheck.setCategory(goods.getCategory());
        goodsCheck.setImg(goods.getImg());
        goodsCheck.setDescription(goods.getDescription());
        goodsCheck.setStock(goods.getStock());
        goodsCheck.setShop(goods.getShop());
        if (goodsCheckMapper.insert(goodsCheck) != 0) {
            return "success.";
        } return "failure";
    }

    @GetMapping("/deleteGoodsByName")
    public String deleteGoodsByName(String name) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        if (goodsMapper.delete(queryWrapper) != 0) {
            return "success.";
        } else return "failure";
    }

    @GetMapping("/getSku")
    public String getSku(String name) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return goodsMapper.selectOne(queryWrapper).getSku();
    }

    @GetMapping("/getGoodsSku")
    public String getGoodsSku() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    /*
    sort:
        0 : 默认排序
        1 : 价格降序
        2 : 价格升序
        3 : 评分降序
        4 : 评分升序
        5 : 销量降序
        6 : 销量升序
    */
    @GetMapping("/searchGoodsByName")
    public List<Goods> searchGoodsByName(String name, int sort) {
        List<Goods> goodsList = goodsMapper.selectList(null);
        if (!Objects.equals(name, "")) {
            System.out.println(name);
            QueryWrapper<Goods> query = new QueryWrapper<>();
            query.like("name", name);
            goodsList = goodsMapper.selectList(query);
            System.out.println(goodsList);
        }
        CommodityComparator commodityComparator = new CommodityComparator();
        switch (sort) {
            case 1: goodsList.sort(commodityComparator::compareByPriceHighToLow);break;
            case 2: goodsList.sort(commodityComparator::compareByPriceLowToHigh);break;
            case 3: goodsList.sort(commodityComparator::compareByScoreHighToLow);break;
            case 4: goodsList.sort(commodityComparator::compareByScoreLowToHigh);break;
            case 5: goodsList.sort(commodityComparator::compareBySalesHighToLow);break;
            case 6: goodsList.sort(commodityComparator::compareBySalesLowToHigh);break;
            default:break;
        }
        return goodsList;
    }

    @GetMapping("/getGoodsBySku")
    public Goods getGoodsBySku(String sku) {
        return goodsMapper.selectById(sku);
    }

    @GetMapping("/getOrderList")
    public List<ShoppingRecord> getOrderList(String shop, int status) {
        QueryWrapper<ShoppingRecord> query = new QueryWrapper<>();
        query.eq("shop", shop).eq("status", status);
        return shoppingRecordMapper.selectList(query);
    }

    @GetMapping("/setOrderList")
    public String setOrderStatus(String orderId, int status) {
        ShoppingRecord shoppingRecord =  shoppingRecordMapper.selectById(orderId);
        shoppingRecord.setStatus(status);
        if (shoppingRecordMapper.updateById(shoppingRecord) != 0) {
            return "success.";
        } else return "failure.";
    }
    @GetMapping("/searchGoodsByCategory")
    public List<Goods> searchGoodsByCategory(String category, int sort) {
        List<Goods> goodsList = goodsMapper.selectList(null);
        if (!Objects.equals(category, "")) {
            QueryWrapper<Goods> query = new QueryWrapper<>();
            query.like("category", category);
            goodsList = goodsMapper.selectList(query);
        }
        CommodityComparator commodityComparator = new CommodityComparator();
        switch (sort) {
            case 1: goodsList.sort(commodityComparator::compareByPriceHighToLow);break;
            case 2: goodsList.sort(commodityComparator::compareByPriceLowToHigh);break;
            case 3: goodsList.sort(commodityComparator::compareByScoreHighToLow);break;
            case 4: goodsList.sort(commodityComparator::compareByScoreLowToHigh);break;
            case 5: goodsList.sort(commodityComparator::compareBySalesHighToLow);break;
            case 6: goodsList.sort(commodityComparator::compareBySalesLowToHigh);break;
            default:break;
        }
        return goodsList;
    }

}
