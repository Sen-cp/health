package com.itheima.health.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemsDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckItemsService.class)
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

        //增加异常管控

        //1.根据删除的id查看有没有和checkgroup进行关联
        Integer counts = checkItemsDao.count(id);
        if (counts > 0){
            //2.如果有则定义一个异常类 抛出自定义异常
            throw new HealthException("想要删除的检查项已被检查组使用,请先删除检查组!");
        }

        Integer rows = checkItemsDao.deleteById(id);
        return rows>0;
    }

    @Override
    public Boolean updateCheckItem(CheckItem checkItem) {

        Integer rows = checkItemsDao.updateCheckItem(checkItem);

        return rows>0;
    }


}
