package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.UserServiceDao;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserServiceDao userServiceDao;

    @Override
    public User findByUsername(String username) {
        return userServiceDao.findByUsername(username);
    }
}
