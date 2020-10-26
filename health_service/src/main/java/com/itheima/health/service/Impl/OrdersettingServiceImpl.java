package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrdersettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrdersettingService.class)
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    private OrdersettingDao ordersettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> list) throws HealthException{
        //首先要查询所有的日期 与列表的日期对比
        for (OrderSetting orderSetting : list) {

            OrderSetting os = ordersettingDao.findOrderDate(orderSetting.getOrderDate());
            //如果os不等于空 说明数据库中存在 判断已预约人数 是否大于设置预约人数 如果是 抛异常
            if (os != null){
                if (os.getReservations()>=orderSetting.getNumber()){
                    throw new HealthException(orderSetting.getOrderDate()+"-设置的预约人数小于或等于已预约人数,请修改后再提交!");
                }
                //存在符合修改规则 修改预约数量
                ordersettingDao.updataNum(orderSetting);
            }else {
                //不存在 添加
                ordersettingDao.add(orderSetting);
            }

        }
    }

    @Override
    public List<Map<String, Integer>> findByYearMoth(String ym) {
        return ordersettingDao.findByYearMoth(ym+"%");
    }

    @Override
    public void setDateOrder(OrderSetting orderSetting) {
        OrderSetting os = ordersettingDao.findOrderDate(orderSetting.getOrderDate());
        //如果os不等于空 说明数据库中存在 判断已预约人数 是否大于设置预约人数 如果是 抛异常
        if (os != null){
            if (os.getReservations()>=orderSetting.getNumber()){
                throw new HealthException(orderSetting.getOrderDate()+"-设置的预约人数小于或等于已预约人数,请修改后再提交!");
            }
            //存在符合修改规则 修改预约数量
            ordersettingDao.updataNum(orderSetting);
        }else {
            //不存在 添加
            ordersettingDao.add(orderSetting);
        }
    }
}
