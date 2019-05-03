package com.br.service.utils;

import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MD5工具类
 * @Author Zero
 * @Date 2019 02 24
 */
public class CrypUtils {

    /**
     * 将源字符串转换为MD5
     * @param source 源字符串
     * @return String
     */
    public String toMD5(String source){
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }


    public static void main(String[] args) {
/*        Date date = new Date();
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf_date.format(date);
        SimpleDateFormat sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetimeString = sdf_datetime.format(date);
        String str = "Datasyx" + dateString + datetimeString + "syx.call.2019wgss.webcall.2019";
        String sign = new CrypUtils().toMD5(str);
        String url = "http://10.2.135.122:8091/CallHandlers/WgssHandler.ashx?MethodName=Data&CallUserName=syx&PlanDate=" + dateString + "&DateTimeToken=" + datetimeString + "&Sign=" + sign;
        System.out.println(url);*/
    }


}
