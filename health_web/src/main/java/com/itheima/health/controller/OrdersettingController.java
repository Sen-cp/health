package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrdersettingService;
import com.itheima.health.utils.POIUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrdersettingService ordersettingService;


    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) throws Exception {
        try {
            //处理数据
            //1.使用工具类,将Excel文档转换为字符串列表数组
            //2.创建一个订单列表
            List<OrderSetting> list = new ArrayList<>();
            List<String[]> excel = POIUtils.readExcel(excelFile);
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

            //将数据转换为List<OrderSetting>
            for (String[] strings : excel) {
                //strings[0] yyyy-MM-dd(string) ---> data
                Date orderDate = sdf.parse(strings[0]);
                Integer setNum = Integer.valueOf(strings[1]);
                OrderSetting orderSetting = new OrderSetting(orderDate, setNum);
                list.add(orderSetting);
            }
            //调用业务层 将数据加入数据库
            ordersettingService.add(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/findByYearMoth")
    public Result findByYearMoth(String ym) {
        //模糊查询预约的数据返回格式 [{ date: 1, number: 120, reservations: 1 },...] list<map<string,string>>
        List<Map<String, Integer>> list = ordersettingService.findByYearMoth(ym);
        return new Result(true, "返回预约数据成功!", list);
    }

    @PostMapping("/setDateOrder")
    public Result setDateOrder(@RequestBody OrderSetting orderSetting) {
        ordersettingService.setDateOrder(orderSetting);
        return new Result(true, "设置预约人数成功!");

    }
}
