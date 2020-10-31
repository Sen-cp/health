package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("clearImg")
public class ClearImg {

    private static final Logger log = LoggerFactory.getLogger(ClearImg.class);

    @Reference
    private SetmealService setmealService;

//    @Scheduled(fixedDelay = 1800000,initialDelay = 3000)
    public void cleanImg(){
        log.info("开始执行垃圾图片删除");

        //获取数据库上所有图片的名称
        List<String> localImg = setmealService.findAllImg();
        log.debug("获取本地照片{}张",localImg.size());

        //获取七牛上的图片
        List<String> qiNiuImg = QiNiuUtils.listFile();
        log.info("获取七牛云照片{}张",qiNiuImg.size());

        //将七牛的减去数据库的就是需要删除的照片
        qiNiuImg.removeAll(localImg);
        log.debug("需要删除的照片有{}张",qiNiuImg.size());

        try {
            //删除剩下的图片
            log.info("开始删除照片");
            List<String> removeFiles = QiNiuUtils.removeFiles(qiNiuImg.toArray(new String[]{}));
            log.info("删除照片成功");
            //log输出一下移除的图片
        } catch (Exception e) {
            log.error("删除照片失败!",e);
        }
    }
}
