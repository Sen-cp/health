package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Map;

public interface OrderDao {
    Integer findByDate(Object orderDate);

    Member findMember(Object telephone);

    Integer findOrder(Map orderInfo);

    Integer submit(Order order);

    Integer insertMember(Member member);

    void updateSetOrder(Date orderDate);

    Map<String, String> findSuccessById(Integer id);
}
