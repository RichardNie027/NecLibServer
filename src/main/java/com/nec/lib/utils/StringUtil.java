package com.nec.lib.utils;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }

    public static String left(String source, int length) {
        int len = source.length();
        return source.substring(0, length > len ? len : length);
    }

    public static String right(String source, int length) {
        int len = source.length();
        return source.substring(length >= len ? 0 : len - length);
    }

    public static String trim(String source) {
        return source.trim();
    }

    public static String trimLeft(String source) {
        if(source.isEmpty())
            return "";
        for(int i=0; i < source.length(); i++) {
            char c = source.charAt(i);
            if(c > 32)
                return source.substring(i);
        }
        return "";
    }

    public static String trimRight(String source) {
        if(source.isEmpty())
            return "";
        for(int i=source.length()-1; i >= 0; i--) {
            char c = source.charAt(i);
            if(c > 32)
                return source.substring(0, i+1);
        }
        return "";
    }

    public static String truncateLeft(String source, int length) {
        int len = source.length();
        if(len <= length)
            return "";
        return source.substring(length, len);
    }

    public static String truncateRight(String source, int length) {
        int len = source.length();
        if(len <= length)
            return "";
        return source.substring(0, len - length);
    }

    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static int parseInt(String str, int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {}
        return result;
    }

    public static float parseFloat(String str, float defaultValue) {
        float result = defaultValue;
        try {
            result = Float.parseFloat(str);
        } catch (NumberFormatException e) {}
        return result;
    }

    public static double parseDouble(String str, double defaultValue) {
        double result = defaultValue;
        try {
            result = Double.parseDouble(str);
        } catch (NumberFormatException e) {}
        return result;
    }

    public static boolean parseBoolean(String str) {
        return Boolean.parseBoolean(str);
    }
}
