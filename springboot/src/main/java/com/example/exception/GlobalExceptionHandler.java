package com.example.exception;

import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回 JSON 格式的响应
    public Result error(Exception e) {
        e.printStackTrace(); // 打印异常堆栈信息
        return Result.error(); // 返回错误响应
    }
}