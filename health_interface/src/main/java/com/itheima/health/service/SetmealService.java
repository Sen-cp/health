package com.itheima.health.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;

import java.util.List;


public interface SetmealService {


    List<CheckGroup> findAllGroup();

    Integer add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findMealById(Integer id);

    List<Integer> findMealGroup(Integer id);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id) throws HealthException;

    List<String> findAllImg();

    List<Setmeal> getSetmeal();

    Setmeal findDetailById(Integer id);
}
