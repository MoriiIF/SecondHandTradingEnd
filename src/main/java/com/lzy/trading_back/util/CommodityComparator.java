package com.lzy.trading_back.util;

import com.lzy.trading_back.entity.Goods;

import java.util.Comparator;

public class CommodityComparator implements Comparator<Goods> {

    public int compareByPriceLowToHigh(Goods o1, Goods o2) {
        if (o1.getPrice() - o2.getPrice() > 0) {
            return 1;
        } else if (o1.getPrice() - o2.getPrice() < 0) {
            return -1;
        } else return 0;
    }

    public int compareByPriceHighToLow(Goods o1, Goods o2) {
        if (o1.getPrice() - o2.getPrice() > 0) {
            return -1;
        } else if (o1.getPrice() - o2.getPrice() < 0) {
            return 1;
        } else return 0;
    }

    public int compareBySalesLowToHigh(Goods o1, Goods o2) {
        return Integer.compare(o1.getSales() - o2.getSales(), 0);
    }

    public int compareBySalesHighToLow(Goods o1, Goods o2) {
        return Integer.compare(0, o1.getSales() - o2.getSales());
    }

    public int compareByScoreLowToHigh(Goods o1, Goods o2) {
        if (o1.getScore() - o2.getScore() > 0) {
            return 1;
        } else if (o1.getScore() - o2.getScore() < 0) {
            return -1;
        } else return 0;
    }

    public int compareByScoreHighToLow(Goods o1, Goods o2) {
        if (o1.getScore() - o2.getScore() > 0) {
            return -1;
        } else if (o1.getScore() - o2.getScore() < 0) {
            return 1;
        } else return 0;
    }

    @Override
    public int compare(Goods o1, Goods o2) {
        return 0;
    }
}
