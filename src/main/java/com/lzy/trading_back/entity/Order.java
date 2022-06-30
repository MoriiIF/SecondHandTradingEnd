package com.lzy.trading_back.entity;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private List<ProductInCart> productInCarts;
    private double sum;
}
