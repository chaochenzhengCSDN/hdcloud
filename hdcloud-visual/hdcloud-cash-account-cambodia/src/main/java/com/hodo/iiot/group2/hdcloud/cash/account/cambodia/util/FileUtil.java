package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    //public static final String RESUME_TEMPLATE = "number.json";
    public static final String RESUME_TEMPLATE = "/opt/bankaccountTongyong/number.json";

    public static JSONObject readJsonFromClassPath(String path){
        //ClassPathResource resource = new ClassPathResource("number.json");
        BufferedReader br = null;
        try {
            //File file = resource.getFile();
            File file = new File(RESUME_TEMPLATE);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String str = br.readLine();
            br.close();
            return JSON.parseObject(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean writeJsonFromClassPath(String path,JSONObject jo){
        //ClassPathResource resource = new ClassPathResource("number.json");
        BufferedWriter bw = null;
        try {
            //File file = resource.getFile();
            File file = new File(RESUME_TEMPLATE);
            if(!file.exists()){
                //file.getParentFile().mkdirs();
                file.createNewFile();
            }
            if(file.canWrite()){
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
                bw.write(jo.toJSONString());
                bw.flush();
                bw.close();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  false;
    }


    /**
     * 封装文件名
     *
     * @param filename
     * @return
     */
    public String getFilename(String filename) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date());
        return filename + "&" + dateString;
    }




}
