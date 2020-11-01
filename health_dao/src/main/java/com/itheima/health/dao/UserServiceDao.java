package com.itheima.health.dao;

import com.itheima.health.pojo.User;

public interface UserServiceDao {
    User findByUsername(String username);
}
