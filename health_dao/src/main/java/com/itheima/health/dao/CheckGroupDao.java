package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    Page<CheckGroup> findPage(String queryPageBean);

    Integer add(CheckGroup checkGroup);

    void addCheckGroupCheckItem(@Param("checkgroupId") Integer id, @Param("checkitemId") Integer checkitemId);

    List<Integer> findCheckItemsById(Integer id);

    //编辑检查组
    void update(CheckGroup checkGroup);
    //删除旧关系 组和项目
    void deleteById(Integer id);

    Integer countGroupSetmeal(Integer id);

    void deleteGroupById(Integer id);
}
