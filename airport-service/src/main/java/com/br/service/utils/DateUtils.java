package com.br.service.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @Author Zero
 * @Date 2019 02 24
 */
public class DateUtils {

    /**
     * 时间戳转换日期
     *
     * @param longTime
     * @return Date
     */
    public Date longToDate(Long longTime) {
        return new Date(longTime);
    }

    /**
     * 日期转换时间戳
     *
     * @param date
     * @return Long
     */
    public Long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 日期转换字符串
     *
     * @param date
     * @return String
     */
    public String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
