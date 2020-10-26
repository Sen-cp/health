package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingDao {


    OrderSetting findOrderDate(Date orderDate);

    void updataNum(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map<String, Integer>> findByYearMoth(String ym);
}
