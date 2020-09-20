package com.hodo.iiot.group2.hdcloudbankaccountshares.util;

import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.PageEntity;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.User;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class jjUtil {
    public final  static  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String handleParams(Map<String, Object> params
            , String key) {
        if (params.get(key) != null) {
            String value = params.get(key).toString();
            params.remove(key);
            return value;
        }
        return null;
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(date!=null){
            return sdf.format(date);
        }else{
            return null;
        }
    }

    //封装用户名和id
    public static String handleUserId(String userId, User user){
        if(user!=null){
            return userId +","+ user.getName();
        }
        return userId;
    }

    //获取当前时间的字符串类型
    public static String getDateStr(){
        Date now = new Date();
        return sdf.format(now);
    }

    //取消摘要中对方单位名称
    public static String handleRemark(String remark,String company){
        if(StringUtil.isNotEmpty(remark)&&StringUtil.isNotEmpty(company)){
            //先判断是否含有内容
            //最后是否还有，标点符号
            if(remark.contains(company)){
                remark = StringUtils.remove(remark,company);
            }
        }
        return remark;
    }



    //处理分页时的page和limit
    public static PageEntity getPageEntity(Map<String,Object> params){
        String page = handleParams(params,"page");
        String limit = handleParams(params,"limit");
        Integer pageInt = 1;
        Integer limitInt = 10;
        if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(page)){
            pageInt = Integer.valueOf(page);
        }
        if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(limit)){
            limitInt = Integer.valueOf(limit);
        }
        PageEntity pageEntity = new PageEntity(pageInt,limitInt);
        return pageEntity;
    }


    //查询时处理金额
    public static String formatMoney(String money){
        if(com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(money)){
            money = null;
        }else{
            money = money.replace(",","");
        }
        return money;
    }



    //检查时间格式是否错误
    public static Boolean checkDateFormat(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }



}
