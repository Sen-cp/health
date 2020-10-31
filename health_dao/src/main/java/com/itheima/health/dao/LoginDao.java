package com.itheima.health.dao;

import java.util.Map;

public interface LoginDao {
    Integer check(String telephone);

    void register(Map<String, String> loginInfo);
}
