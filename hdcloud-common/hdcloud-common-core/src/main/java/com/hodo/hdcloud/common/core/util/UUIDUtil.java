package com.hodo.hdcloud.common.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by wzy
 * 2019/7/24.
 */
public class UUIDUtil
{
    public final static String DEFAILT_DATE_TIME_PATTERN = "yyMMddHHmmss";
    public final static String DATE_PATTERN = "yyMMdd";

    /**
     * UUID是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。
     * * 算法的核心思想是结合机器的网卡、当地时间、一个随机数来生成GUID。
     * * 从理论上讲，如果一台机器每秒产生10000000个GUID，则可以保证（概率意义上）3240年不重复。
     *
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 获取16位的字符串
     *
     * @return
     */
    public static String getRandomUserId() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DEFAILT_DATE_TIME_PATTERN);
        String dateString = format.format(date);
        int randomNumber = (int) ((Math.random() * 9 + 1) * 1000);
        return dateString + randomNumber;
    }


    public static String getRandomId() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        String dateString = format.format(date);
        int randomNumber = (int) ((Math.random() * 9 + 1) * 1000);
        return dateString + randomNumber;
    }

    public static String randomString(int length) {
        String character = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(character.charAt(number));
        }
        return sb.toString();
    }

    public static String getInviteCode() {
        String character = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            sb.append(character.charAt(random.nextInt(62)));
        }
        return sb.toString();
    }

    public static String getRandomString() {
        String character = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            sb.append(character.charAt(random.nextInt(36)));
        }
        return sb.toString();
    }


    public static String getBatchNumber() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String dateString = format.format(date);
        return dateString + randomString(4);
    }

    public static String getRedPacketCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("HB");
        sb.append(randomString(8));
        return sb.toString();
    }

    public static String getCautionCode() {
        String character = "zxcvbnmlkjhgfdsaqwertyuipQWERTYUOPASDFGHJKLZXCVBNM";
        String number = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(character.charAt(random.nextInt(50)));
        for (int i = 0; i < 6; ++i) {
            sb.append(number.charAt(random.nextInt(10)));
        }
        return sb.toString();
    }

    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };
    /**
     * 8位uuid
     *
     * @return
     */
    public static String uuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
