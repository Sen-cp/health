package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo){

        Order order = orderService.submit(orderInfo);
        return new Result(true,"预约成功!",order);
    }

    @PostMapping("/findById")
    public Result findSuccessById(Integer id){
        Map<String,String> map = orderService.findSuccessById(id);
        return new Result(true,"查询成功",map);
    }

}
