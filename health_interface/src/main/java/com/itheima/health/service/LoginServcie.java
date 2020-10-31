package com.itheima.health.service;

import com.itheima.health.exception.HealthException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginServcie {
    void check(Map<String, String> loginInfo) throws HealthException;
}
