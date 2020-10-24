package com.itheima.health.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<CheckGroup> pages = checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult<>(pages.getTotal(),pages.getResult());
    }

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer id = checkGroup.getId();
        for (Integer checkitemId : checkitemIds) {
            checkGroupDao.addCheckGroupCheckItem(id,checkitemId);
        }
    }

    @Override
    public List<Integer> findCheckItemsById(Integer id) {

        return checkGroupDao.findCheckItemsById(id);
    }

    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkItems) {
        checkGroupDao.update(checkGroup);
        Integer id = checkGroup.getId();
        //删除关系表中的旧关系
        checkGroupDao.deleteById(id);
        //新建关系
        for (Integer checkItem : checkItems) {
            checkGroupDao.addCheckGroupCheckItem(id,checkItem);
        }
    }

    @Override
    public void deleteGroupById(Integer id) {
        //增加异常管控

        //1.根据删除的id查看有没有和checkgroup进行关联
        Integer counts = checkGroupDao.countGroupSetmeal(id);
        if (counts > 0){
            //2.如果有则定义一个异常类 抛出自定义异常
            throw new HealthException("想要删除的检查组已被检查套餐使用,请先删除检查套餐!");
        }

        checkGroupDao.deleteById(id);

        checkGroupDao.deleteGroupById(id);

    }
}
