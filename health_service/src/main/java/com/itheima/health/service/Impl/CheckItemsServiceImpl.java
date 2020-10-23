package com.itheima.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.CheckItemsDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckItemsServiceImpl implements CheckItemsService {

    @Autowired
    private CheckItemsDao checkItemsDao;
    @Override
    public List<CheckItem> findAll() {
       return checkItemsDao.findAll();
    }

    @Override
    public Boolean add(CheckItem checkItem) {

       Integer rows =  checkItemsDao.add(checkItem);
       return rows > 0;
    }

    @Override
    public PageResult<CheckItem> findByPage(QueryPageBean queryPageBean) {


        List<CheckItem> list = checkItemsDao.findByPage(queryPageBean   );
        String queryString = queryPageBean.getQueryString();
        Long total = null;
        if (!queryString.isEmpty()) {

            total = checkItemsDao.findTotal(queryString);
        }else{
            total = checkItemsDao.findTotal(null);
        }
        return new PageResult<>(total, list);
    }
}
