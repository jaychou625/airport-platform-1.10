package com.br.service.utils;

import org.springframework.util.DigestUtils;

/**
 * MD5工具类
 * @Author Zero
 * @Date 2019 02 24
 */
public class MD5Utils {

    /**
     * 将源字符串转换为MD5
     * @param source 源字符串
     * @return String
     */
    public String toMD5(String source){
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }


    public static void main(String[] args) {
        String str = "Datasyx2019042520190425103820syx.call.2019wgss.webcall.2019";
        System.out.println(new MD5Utils().toMD5(str));
    }


}
