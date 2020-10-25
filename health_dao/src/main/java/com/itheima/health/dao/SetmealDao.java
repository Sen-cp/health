package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {
    List<CheckGroup> findAllGroup();

    void addMeal(Setmeal setmeal);

    void addMealGroup(@Param("mealId") Integer id, @Param("groupId") Integer checkgroupId);

    Page<Setmeal> findPage(String queryString);

    Setmeal findMealById(Integer id);

    List<Integer> findMealGroup(Integer id);

    void update(Setmeal setmeal);

    void deleteMealGroup(Integer id);


    Integer count(Integer id);

    void delete(Integer id);
}
