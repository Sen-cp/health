package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Override
    public List<CheckGroup> findAllGroup() {
        return setmealDao.findAllGroup();
    }

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息 返回id
        setmealDao.addMeal(setmeal);
        Integer id = setmeal.getId();
        for (Integer checkgroupId : checkgroupIds) {
            setmealDao.addMealGroup(id,checkgroupId);
        }
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Setmeal> pages= setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(pages.getTotal(),pages.getResult());
    }

    @Override
    public Setmeal findMealById(Integer id) {
        return setmealDao.findMealById(id);
    }

    @Override
    public List<Integer> findMealGroup(Integer id) {
        return setmealDao.findMealGroup(id);
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //跟新套餐
        setmealDao.update(setmeal);
        //删除套餐组关系
        Integer id = setmeal.getId();
        setmealDao.deleteMealGroup(id);
        //插入套餐组关系
        if (checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addMealGroup(id,checkgroupId);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        //查询订单与套餐的关系
        Integer rows = setmealDao.count(id);
        if (rows > 0){
            throw new HealthException("套餐已存在订单,不可删除!");
        }
        //删除和组之间的关系
        setmealDao.deleteMealGroup(id);
        //删除套餐
        setmealDao.delete(id);
    }

    @Override
    public List<String> findAllImg() {

       return setmealDao.findAllImg();
    }
}
