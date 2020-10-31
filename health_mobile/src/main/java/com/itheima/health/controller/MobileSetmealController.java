package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;

import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.PrintStream;
import java.util.List;

@RestController
@RequestMapping("/mobileSetmeal")
public class MobileSetmealController {


    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

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

    @GetMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        //拼接一下图片地址
        String domain = QiNiuUtils.DOMAIN;
        String url = domain +  setmeal.getImg();
        setmeal.setImg(url);
        return new Result(true,"查询套餐成功!",setmeal);
    }

    @GetMapping("/send4Order")
    public Result send4Order(String telephone){
        String key = RedisMessageConstant.SENDTYPE_ORDER +"_" + telephone;
        Jedis jedis = jedisPool.getResource();
       if ( !jedis.exists(key)){
           //生成验证码
           String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
           try {
               //发送短信
               SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
               //发送验证码(成功)//存入Redis
               jedis.setex(key,5*60,code);
               jedisPool.close();
               return new Result(true,"短信发送成功!");
           } catch (ClientException e) {
               e.printStackTrace();
               throw new HealthException("发送短信失败");
           }

       }else {
           return new Result(false,"短信已发送,请稍后再试!");
       }

    }
}
