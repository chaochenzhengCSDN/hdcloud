package com.hodo.iiot.group2.hdcloud.bank.account.currency.util;



import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    public static long getMultiDay(Date date){
        long time = new Date().getTime()-date.getTime();
        long day = time/1000/60/60/24;
        return day;
    }

    //处理前端传入参数
//    public void filterParam(Map<>){
//        if(params!=null&&params.size()>0){
//            for(Map<String,Object> entry:params){
//
//            }
//        }
//    }
    /**
     * 封装文件名
     *
     * @param filename
     * @return
     */
    public static String getFilename(String filename) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date());
        return filename + "&" + dateString;
    }

    public static String formatNo(String num){
        if(StringUtil.isNotEmpty(num)){
            while (num.length()<6){
                num = "0"+num;
            }
        }
        return num;
    }

    //判断是否加入参数集合里
    public static void addParams(Map<String,Object> params,String key,String param){
        if(StringUtil.isNotEmpty(param)){
            params.put(key,param);
        }
    }

    /**
          * Map转成实体对象
          * @param map map实体对象包含属性
          * @param clazz 实体对象类型
          * @return
          */
   public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
       if (map == null) {
           return null;
       }
       Object obj = null;
       try {
           obj = clazz.newInstance();


           Field[] fields = obj.getClass().getDeclaredFields();
           for (Field field : fields) {
               int mod = field.getModifiers();
               if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                   continue;
               }
               field.setAccessible(true);
               field.set(obj, map.get(field.getName()));
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return obj;
   }

}
