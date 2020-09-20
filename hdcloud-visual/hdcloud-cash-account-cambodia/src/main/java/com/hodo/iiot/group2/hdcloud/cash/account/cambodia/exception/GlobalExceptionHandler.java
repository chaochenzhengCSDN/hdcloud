package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.exception;


import com.hodo.hdcloud.common.core.util.R;
import com.thoughtworks.xstream.core.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 全局异常捕获
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler()
    @ResponseBody
    public R exceptionHandle(Exception e){ // 处理方法参数的异常类型
        e.printStackTrace();
        System.out.println(e.getMessage());
        return R.failed("异常，请联系管理员");//自己需要实现的异常处理
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public R handle(BaseException e){

        e.printStackTrace();
        return R.failed("运行异常，请联系管理员");//自己需要实现的异常处理
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public R handle(SQLException e){
        e.printStackTrace();
        return R.failed("sql错误，请联系管理员");//自己需要实现的异常处理
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public R handle(IOException e){

        e.printStackTrace();
        return R.failed("io错误，请联系管理员");//自己需要实现的异常处理
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R handle(IllegalArgumentException e){
        e.printStackTrace();
        return R.failed("sql字段名错误，请检查后联系管理员");//自己需要实现的异常处理
    }
}
