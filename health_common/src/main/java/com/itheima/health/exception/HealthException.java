package com.itheima.health.exception;

public class HealthException extends RuntimeException {

    /**
     * Description: 自定义异常
     * 友好提示
     * 区分系统与自定义的异常
     * 终止已经不符合业务逻辑的代码
     * User: Sen
     */

    public HealthException(String message){
        super(message);
    }

}
