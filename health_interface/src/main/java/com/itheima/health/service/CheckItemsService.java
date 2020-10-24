package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemsService {

    List<CheckItem> findAll();

    Boolean add(CheckItem checkItem);

    PageResult<CheckItem> findByPage(QueryPageBean queryPageBean);

    Boolean deleteById(Integer id) throws HealthException;

    Boolean updateCheckItem(CheckItem checkItem);

}
