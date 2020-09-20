package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.text.SimpleDateFormat;
import java.util.*;

public class jjUtil {
    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String handleParams(Map<String, Object> params
            , String key) {
        if (params.get(key) != null) {
            String value = params.get(key).toString();
            params.remove(key);
            return value;
        }
        return null;
    }

    public static QueryWrapper handleAllParamsToWrapper(Map<String, Object> params
            , QueryWrapper queryWrapper) {
        Set<Map.Entry<String, Object>> set = params.entrySet();
        List strs = Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"
                , "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"});
        for (Map.Entry e : set) {
            String key = e.getKey().toString();
            StringBuffer sb = new StringBuffer();
            char[] chars = key.toCharArray();
            for (int i = 0; i < key.length(); i++) {
                if (strs.contains(String.valueOf(chars[i]))) {
                    sb.append("_").append(String.valueOf((char) (chars[i] + 32)));
                } else {
                    sb.append(String.valueOf(chars[i]));
                }
            }
            if (key.endsWith("Start")) {
                queryWrapper.ge(sb.toString().substring(0,sb.toString().length()-6), e.getValue());
            } else if (key.endsWith("End")) {
                queryWrapper.lt(sb.toString().substring(0,sb.toString().length()-4), e.getValue());
            }else if(key.endsWith("Like")){
                queryWrapper.like(sb.toString().substring(0,sb.toString().length()-5),e.getValue());
            } else {
                queryWrapper.eq(sb.toString(), e.getValue());
            }
        }
        return queryWrapper;
    }

    /**
     * 清除MAP中null或空字符串参数
     *
     * @param params
     */
    public static void handleParamsClean(Map<String, Object> params) {
        Iterator<Map.Entry<String, Object>> set = params.entrySet().iterator();
        while (set.hasNext()) {
            Map.Entry<String, Object> map = set.next();
            if (map.getValue() == null || map.getValue().toString().length() == 0) {
                set.remove();
            }
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            return sdf.format(date);
        } else {
            return null;
        }
    }


    //获取当前时间的字符串类型
    public static String getDateStr() {
        Date now = new Date();
        return sdf.format(now);
    }





}
