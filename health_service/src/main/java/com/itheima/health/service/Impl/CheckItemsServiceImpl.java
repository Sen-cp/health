package com.itheima.health.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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


        /*List<CheckItem> list = checkItemsDao.findByPage(queryPageBean   );
        String queryString = queryPageBean.getQueryString();
        Long total = null;
        if (!queryString.isEmpty()) {

            total = checkItemsDao.findTotal(queryString);
        }else{
            total = checkItemsDao.findTotal(null);
        }*/

        //--------------------------------------------------------
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 有查询条件，拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<CheckItem> page = checkItemsDao.findByPage(queryPageBean.getQueryString());

        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public Boolean deleteById(Integer id) {

        Integer rows = checkItemsDao.deleteById(id);
        return rows>0;
    }

    @Override
    public Boolean updateCheckItem(CheckItem checkItem) {

        Integer rows = checkItemsDao.updateCheckItem(checkItem);

        return rows>0;
    }
}
