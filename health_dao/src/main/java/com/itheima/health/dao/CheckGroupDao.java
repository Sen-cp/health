package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

public interface CheckGroupDao {
    Page<CheckGroup> findPage(String queryPageBean);

    Integer add(CheckGroup checkGroup);

    void addCheckGroupCheckItem(@Param("checkgroupId") Integer id, @Param("checkitemId") Integer checkitemId);
}
