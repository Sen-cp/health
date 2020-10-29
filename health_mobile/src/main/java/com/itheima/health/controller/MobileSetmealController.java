package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintStream;
import java.util.List;

@RestController
@RequestMapping("/mobileSetmeal")
public class MobileSetmealController {

    @Reference
    private SetmealService setmealService;

    //页面展示--查询所有的套餐信息
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> setmeals = setmealService.getSetmeal();
        //拼接一下图片地址
        String domain = QiNiuUtils.DOMAIN;
        for (Setmeal setmeal : setmeals) {
            String url = domain +  setmeal.getImg();
            setmeal.setImg(url);
        }
        return new Result(true,"查询套餐成功!",setmeals);
    }

    @GetMapping("/findDetailById")
    public Result findDetailById(Integer id){
        Setmeal setmeal = setmealService.findDetailById(id);
        //拼接一下图片地址
        String domain = QiNiuUtils.DOMAIN;
        String url = domain +  setmeal.getImg();
        setmeal.setImg(url);
        return new Result(true,"查询套餐成功!",setmeal);
    }

}
