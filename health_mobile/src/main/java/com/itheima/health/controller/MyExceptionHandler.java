package com.itheima.health.controller;


import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(HealthException.class)
    public Result handleMyException(HealthException e){

        return new Result(false,e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生未知异常",e);
        return new Result(false,"发生未知异常,请联系管理员");
    }


}
