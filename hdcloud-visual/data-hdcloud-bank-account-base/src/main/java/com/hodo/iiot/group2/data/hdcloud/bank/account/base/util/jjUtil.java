package com.hodo.iiot.group2.data.hdcloud.bank.account.base.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.map.HashedMap;

import java.text.ParseException;
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
        Map<String, Object> params2 = new HashedMap();
        params2.putAll(params);
        List strs = Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"
                , "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"});
        Map temp = new HashMap();
        Iterator<Map.Entry<String, Object>> ite  = set.iterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //处理开始等于结束的情况
        for (Map.Entry e : set) {
            String key = e.getKey().toString();
            if(key.endsWith("Start")){
                String str = key.substring(0,key.length()-5);
                    temp.put(str,e.getValue());
            }
        }
        try{
            for (Map.Entry e : set) {
                String key = e.getKey().toString();
                if(key.endsWith("End")){
                    String str = key.substring(0,key.length()-3);
                    String startVal = String.valueOf(temp.get(str));
                    String endVal = String.valueOf(e.getValue());
                    long start = sdf.parse(startVal).getTime();
                    long end = sdf.parse(endVal).getTime();
                    if(start == end){
                        params2.remove(key);
                        params2.remove(str+"Start");
                        params2.put(str,startVal);
                    }
                }
            }
        }catch (ParseException e){
            e.printStackTrace();
            return new QueryWrapper();
        }

        //设置查询参数
        Set<Map.Entry<String, Object>> set2 = params2.entrySet();
        for (Map.Entry e : set2) {
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
