package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GenerateHtmlJob {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    @Autowired
    private Configuration configuration;

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @Value("${out_put_path}")
    private String out_put_path;

    @PostConstruct
    public void init(){
        configuration.setClassForTemplateLoading(this.getClass(),"/ftl");
        configuration.setDefaultEncoding("utf-8");
    }

    /**
     * 任务执行的方法
     */
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void doGenerateHtml(){
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Set<String> ids = jedis.smembers(key);
        for (String id : ids) {
            String[] split = id.split("\\|");
            String setmealId = split[0];
            String operator = split[1];
            if ("1".equals(operator)){
                //生成套餐详情页面

            }else if("0".equals(operator)){
                //删除页面
            }
            //生成套餐列表页面
            try {
                generateSetmealList();
                log.info("生成套餐页面成功");
            } catch (Exception e) {
                log.error("生成套餐页面失败",e);
            }
        }
    }

    private void generateSetmealList() throws Exception{
        Template template = configuration.getTemplate("mobile_setmeal.ftl");
        Map<String, Object> dataMap = new HashMap<>();
        List<Setmeal> setmealList = setmealService.getSetmeal();
        dataMap.put("setmealList",setmealList);
        setmealList.forEach(s->s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        String filename = out_put_path + "/mobile_setmeal.html";
                BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
        template.process(dataMap,writer);
        writer.flush();
        writer.close();
    }

}
