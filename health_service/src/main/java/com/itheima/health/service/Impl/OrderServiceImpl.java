package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Order submit(Map<String, String> orderInfo) {

        //1. 查询预约表 查看该日期下是否约满
        Integer rows = orderDao.findByDate(orderInfo.get("orderDate"));
        if (rows <= 0) {
            //约满 返回预约失败 请更换预约日期
            throw new HealthException("选择的日期已约满");
        }
        //未约满
        // 校验验证码
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + orderInfo.get("telephone");
        String code = jedis.get(key);
        if (null == code) {
            //不存在 ---> 返回验证码输入有误
            throw new HealthException("验证码输入有误");
        }
        //Redis查询验证码 ---> 存在 则比较与用户提交的 是否相同
        String validateCode = orderInfo.get("validateCode");
        if (!code.equals(validateCode)) {
            //不相同 ---> 返回验证码错误 请重试
            throw new HealthException("验证码输入有误");
        }
        //相同 --->校验通过 删除Redis缓存
        jedis.del(key);
        //插入预约方式:orderType
        orderInfo.put("orderType", "微信预约");
        //2.查询是否有重复预约 条件:同一天 同一个人 同一个套餐
        //(1) 查询用户电话号码是否存在于用户表
        Member member1 = orderDao.findMember(orderInfo.get("telephone"));

        SimpleDateFormat sdf = null;
        //修改成日期类型
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = orderInfo.get("orderDate");
        Date orderDate = null;
        try {
            orderDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new HealthException("日期类型解析异常");
        }

        if (member1 != null) {
            orderInfo.put("member_id",String.valueOf(member1.getId()));
            //存在 --> 查询此用户是否满足条件:同一天 同一个人 同一个套餐
            //满足 ---> 返回异常 用户重复预约 预约无效
            Integer orderRows = orderDao.findOrder(orderInfo);
            if (orderRows > 0) {
                throw new HealthException("重复预约当天同一套餐,请确认!");
            } else {
                //不满足 ---> 进行预约
                //插入用户id
                Order order1 = new Order();
                order1.setMemberId(member1.getId());
                order1.setOrderDate(orderDate);
                order1.setOrderType(Order.ORDERTYPE_WEIXIN);
                order1.setOrderStatus(Order.ORDERSTATUS_NO);
                order1.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
                orderDao.submit(order1);
                orderDao.updateSetOrder(orderDate);
                return order1;
            }
        } else {
            Member member = new Member();

            // name 从前端来
            member.setName(orderInfo.get("name"));
            // sex  从前端来
            member.setSex(orderInfo.get("sex"));
            // idCard 从前端来
            member.setIdCard(orderInfo.get("idCard"));
            // phoneNumber 从前端来
            member.setPhoneNumber(orderInfo.get("telephone"));
            // regTime 系统时间
            member.setRegTime(new Date());
            // password 可以不填，也可生成一个初始密码
            member.setPassword("12345678");
            // remark 自动注册
            member.setRemark("由预约而注册上来的");
            orderDao.insertMember(member);

            //不存在 ---> 则不会存在重复套餐 ---> 先进行用户注册(往用户表插入用户数据) ---> 进行预约
//            orderInfo.put("regTime", sdf.format(new Date()));
//            orderInfo.put("password", String.valueOf(orderInfo.get("idCard")).substring
//                    (String.valueOf(orderInfo.get("idCard")).length() - 6));
//            orderInfo.put("member_id",String.valueOf(member.getId()));
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(orderDate);
            order.setOrderType(Order.ORDERTYPE_WEIXIN);
            order.setOrderStatus("未到诊");
            order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
            orderDao.submit(order);

            //成功后需要修改预约表数量
            orderDao.updateSetOrder(orderDate);
            return order;
        }
    }

    @Override
    public Map<String, String> findSuccessById(Integer id) {
        return orderDao.findSuccessById(id);
    }
}
