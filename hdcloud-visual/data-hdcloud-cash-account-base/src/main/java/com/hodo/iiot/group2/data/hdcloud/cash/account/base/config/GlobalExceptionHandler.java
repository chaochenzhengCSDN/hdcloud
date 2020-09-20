package com.hodo.iiot.group2.data.hdcloud.cash.account.base.config;

import com.hodo.hdcloud.common.core.util.R;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.zip.DataFormatException;

/**
 * 全局异常捕获处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  

        /**
         * http请求的方法不正确
         */
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        @ResponseBody
        public R httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
            return R.failed("请求方法不正确");
        }

        /**
         * 请求参数不全
         */
        @ExceptionHandler(MissingServletRequestParameterException.class)
        @ResponseBody
        public R missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
            return R.failed("请求参数不全");
        }

        /**
         * 请求参数类型不正确
         */
        @ExceptionHandler(TypeMismatchException.class)
        @ResponseBody
        public R typeMismatchExceptionHandler(TypeMismatchException e) {
            return R.failed("请求参数类型不正确");
        }

        /**
         * 数据格式不正确
         */
        @ExceptionHandler(DataFormatException.class)
        @ResponseBody
        public R dataFormatExceptionHandler(DataFormatException e) {
            return R.failed("数据格式不正确");
        }


        /**
         * 非法输入
         */
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public R illegalArgumentExceptionHandler(IllegalArgumentException e) {
            return R.failed("非法输入");
        }


    /**
     * 数据库字段重复值
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public R duplicateKeyExceptionHandler(DuplicateKeyException e) {
        return R.failed("字段有重复值");
    }

    /**
     * mybatis错误
     */
    @ExceptionHandler(PersistenceException.class)
    @ResponseBody
    public R persistenceExceptionHandler(PersistenceException e) {
        e.printStackTrace();
        if(e.getMessage().contains("Duplicate entry")){
            return R.failed("请检查数据是否有重复");
        }
        if(e.getMessage().contains("Data too long")){
            return R.failed("请检查数据是否过长");
        }
        return R.failed("请检查数据是否正确");
    }

        @ExceptionHandler  //处理其他异常
        @ResponseBody
        public R allExceptionHandler(Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            return R.failed("服务器异常");
        }
    
}
