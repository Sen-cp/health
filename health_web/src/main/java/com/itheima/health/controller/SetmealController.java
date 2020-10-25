package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    //将图片上传到七牛云
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try {
            //获取图片的名字
            String originalFilename = imgFile.getOriginalFilename();
            //获取图片的后缀
            assert originalFilename != null;
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //生成照片唯一名字
            String imgName = UUID.randomUUID() + extension;
            //调用七牛云存储图片信息
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),imgName);
            //将数据存入map集合 返回前端
            Map<String, String> imgMap = new HashMap<>();
            imgMap.put("imgName",imgName);
            imgMap.put("domain",QiNiuUtils.DOMAIN);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,imgMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    //查询所有的group用list封装
    @GetMapping("/findAllGroup")
    public Result findAllGroup(){
        List<CheckGroup> list = setmealService.findAllGroup();
        return new Result(true,"查询所有的检查组成功!",list);
    }

    //添加检查套餐
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true,"新增套餐成功!");
    }

    //分页查询检查套餐
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> list = setmealService.findPage(queryPageBean);
        return new Result(true,"查询检查套餐成功!",list);
    }

    //编辑检查套餐--回显数据
    @GetMapping("/findMealById")
    public Result findMealById(Integer id){
        Setmeal setmeal = setmealService.findMealById(id);
        String domain = QiNiuUtils.DOMAIN;
        Map<String, Object> map = new HashMap<>();
        map.put("formData",setmeal);
        map.put("url",domain);
        return new Result(true,"回显套餐数据成功!",map);
    }

    //编辑套餐---回显勾选组信息
    @GetMapping("/findMealGroup")
    public Result findMealGroup(Integer id){
        List<Integer> list = setmealService.findMealGroup(id);
        return new Result(true,"回显勾选组数据成功!",list);
    }

    //编辑套餐--编辑
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){

        setmealService.update(setmeal,checkgroupIds);
        return new Result(true,"编辑数据成功!");

    }

    //删除套餐
    @PostMapping("/deleteById")
    public Result delete(Integer id){
        setmealService.delete(id);
        return new Result(true,"删除套餐成功!");
    }
}
