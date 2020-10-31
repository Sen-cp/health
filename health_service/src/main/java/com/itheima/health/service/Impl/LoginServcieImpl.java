package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.dao.LoginDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.service.LoginServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;


@Service(interfaceClass = LoginServcie.class)
public class LoginServcieImpl implements LoginServcie {

    private static final Logger log = LoggerFactory.getLogger(LoginServcieImpl.class);

    @Autowired
    private LoginDao loginDao;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void check(Map<String, String> loginInfo) {
        //验证验证码是否正确
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + loginInfo.get("telephone");
        String code = jedis.get(key);
        if (code.equals(loginInfo.get("validateCode"))){
            //去数据库看看有没有这个B
            Integer rows = loginDao.check(loginInfo.get("telephone"));
            //有 登录成功
            if (rows>0){
                log.info("用户在登录...");

            }else {
                //没有 帮他注册
                loginInfo.put("remark","登录页面注册");
                loginDao.register(loginInfo);
                //保存用户登录信息


            }
        }else{
            throw new HealthException("验证码校验不通过");
        }
    }
}
