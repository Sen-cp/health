package com.itheima.health.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.service.LoginServcie;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private LoginServcie loginServcie;

    @Autowired
    private JedisPool jedisPool;

    //发送登录短信
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //获取key
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String code = jedis.get(key);
        if (code == null){
            //生成code
            Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
            //短信发送code
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,String.valueOf(validateCode));
            } catch (ClientException e) {
               throw  new HealthException("发送短信失败");
            }
            //成功存入Redis 5分钟
            jedis.setex(key,5*60,String.valueOf(validateCode));
            jedisPool.close();
            return new Result(true,"短信发送成功");
        }else {
            throw new HealthException("信息已发送,请注意查收");
        }
    }

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> loginInfo,HttpServletResponse resp){
        loginServcie.check(loginInfo);

//        Cookie cookie = new Cookie("login_member_telephone", loginInfo.get("telephone"));
//                cookie.setMaxAge(30*24*60*60);
//                cookie.setPath("/");
        return new Result(true,"登录成功");
    }

}
