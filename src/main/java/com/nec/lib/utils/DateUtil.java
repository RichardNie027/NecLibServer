package com.nec.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String DATEFORMATSTRING = "yyyy-MM-dd HH:mm:ss";

    /**
     * java.util.Date转String（格式：yyyy-MM-dd HH:mm:ss）
     * @param date
     * @return
     */
    public static String toStr(Date date) {
        return toStr(date, DATEFORMATSTRING);
    }

    /**
     * java.util.Date转String（格式自定义，默认：yyyy-MM-dd HH:mm:ss）
     * @param date
     * @param dateFormatString
     * @return
     */
    public static String toStr(Date date, String dateFormatString) {
        if(dateFormatString == null || dateFormatString.equals(""))
            dateFormatString = DATEFORMATSTRING;
        return new SimpleDateFormat(dateFormatString).format(date);
    }

    /**
     * Calendar转String（格式：yyyy-MM-dd HH:mm:ss）
     * @param calendar
     * @return
     */
    public static String toStr(Calendar calendar) {
        return toStr(calendar, DATEFORMATSTRING);
    }

    /**
     * Calendar转String（格式自定义，默认：yyyy-MM-dd HH:mm:ss）
     * @param calendar
     * @param dateFormatString
     * @return
     */
    public static String toStr(Calendar calendar, String dateFormatString) {
        if(dateFormatString == null || dateFormatString.equals(""))
            dateFormatString = DATEFORMATSTRING;
        return new SimpleDateFormat(dateFormatString).format(calendar.getTime());
    }

    public static Date fromStr(String dateStr) {
        return fromStr(dateStr, DATEFORMATSTRING);
    }
    /**
     * String（格式：yyyy-MM-dd HH:mm:ss）转java.util.Date
     * 时间格式若不完整，自动补零
     * @param dateStr
     * @return 格式错误返回null
     */
    public static Date fromStr(String dateStr, String dateFormatString) {
        String template = dateFormatString.replace("yyyy", "1970");
        template = template.replace("MM", "01");
        template = template.replace("dd", "01");
        template = template.replace("HH", "00");
        template = template.replace("mm", "00");
        template = template.replace("ss", "00");
        dateStr = dateStr + template.substring(dateStr.length());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Calendar fromString(String dateStr) {
        return fromString(dateStr, DATEFORMATSTRING);
    }
    /**
     * String（格式：yyyy-MM-dd HH:mm:ss）转Calendar
     * 时间格式若不完整，自动补零
     * @param dateStr
     * @return 格式错误返回null
     */
    public static Calendar fromString(String dateStr, String dateFormatString) {
        Date date = fromStr(dateStr, dateFormatString);
        Calendar calendar = Calendar.getInstance();
        if(date != null) {
            calendar.setTime(date);
            return calendar;
        } else
            return null;
    }

}
