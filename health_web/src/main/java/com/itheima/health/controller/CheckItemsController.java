package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkItems")
public class CheckItemsController {

    @Reference()
    private CheckItemsService checkItemsService;

    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> list = checkItemsService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
    }


    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        Boolean flag = checkItemsService.add(checkItem);
        return new Result(flag,flag?MessageConstant.ADD_CHECKITEM_SUCCESS:MessageConstant.ADD_CHECKITEM_FAIL);
    }

    @PostMapping("/findByPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<CheckItem> pageResult = checkItemsService.findByPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }
}
