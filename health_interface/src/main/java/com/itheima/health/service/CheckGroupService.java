package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    List<Integer> findCheckItemsById(Integer id);

    void update(CheckGroup checkGroup, Integer[] checkItems);

    void deleteGroupById(Integer id) throws HealthException;
}
