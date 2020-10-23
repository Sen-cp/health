package com.itheima.health.dao;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemsDao {

    List<CheckItem> findAll();

    Integer add(CheckItem checkItem);

    List<CheckItem> findByPage(QueryPageBean queryPageBean);

    Long findTotal(String queryString);
}
