package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemsDao {

    List<CheckItem> findAll();

    Integer add(CheckItem checkItem);

    Page<CheckItem> findByPage(String queryPageBean);

    Long findTotal(String queryString);

    Integer deleteById(Integer id);

    Integer updateCheckItem(CheckItem checkItem);
}
