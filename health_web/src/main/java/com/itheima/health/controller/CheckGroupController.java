package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckGroup> pageResult =
                checkGroupService.findPage(queryPageBean);

        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }


    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupService.add(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @GetMapping("/findCheckItemsById")
    public Result findCheckItemsById(Integer id){
        List<Integer> checkItemsIds = checkGroupService.findCheckItemsById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemsIds);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        checkGroupService.update(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/deleteGroupById")
    public Result deleteGroupById(Integer id){
        checkGroupService.deleteGroupById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
