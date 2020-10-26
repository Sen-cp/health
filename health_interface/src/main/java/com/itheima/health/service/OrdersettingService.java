package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrdersettingService {


    void add(List<OrderSetting> list) throws HealthException;

    List<Map<String, Integer>> findByYearMoth(String ym);

    void setDateOrder(OrderSetting orderSetting) throws HealthException;
}
