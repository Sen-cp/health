package com.itheima.health.job;

import org.springframework.scheduling.annotation.Scheduled;

public class GenerateHtmlJob {

    /**
     * 任务执行的方法
     */
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void doGenerateHtml(){
        //
    }

}
