package com.br.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 */
public class TransitionUtil {
    /**
     * 16进制转ASCII
     *
     * @param hex
     * @return
     */
    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {
            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * 16进制转化bit
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * bit转换16进制
     */
    public static String binaryString2hexString(String binaryString){
        String hex = Integer.toString (Integer.parseInt (binaryString, 2), 16);
        return hex.toUpperCase();
    }

    /**
     * 16进制转时间戳字符串
     */
    public static  String hexString2TimeStamp(String hexString){
        String date = new BigInteger(hexString,16).toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        date = sdf.format(new Date(Long.valueOf(time)));
        return date;
    }

    /**
     * 小端传输字符转换
     */
    public static  String toLittle(String str){
        String s = "";
        char[] sChar = str.toCharArray();
        String[] sArr = new String[8];
        for(int i = 0; i < sChar.length; i+=2){
            sArr[i / 2] = String.valueOf(sChar[i]) + String.valueOf(sChar[i + 1]);
        }
        ArrayList<String> arraylist = new ArrayList<String>();
        for (int i = 0; i < sArr.length; i++) {
            arraylist.add(sArr[i]); //存放元素
        }
        Collections.reverse(arraylist);
        s = String.join(",",  arraylist.toArray(new String[arraylist.size()]));
        s = s.replaceAll(","," ");
        s = s.replaceAll(" ","");

        return  s;
    }

    /**
     *探头数据转换
     */
    public static String radarTransfor(String str){
        String s = "";
        if(str.equals("FC")){
            s = "探头未探测到障碍物";
        }else if(str.equals("FD")){
            s = "探头未插入";
        }else{
            double temp = Integer.parseInt(str,16);
            temp = temp / 100;
            s = String.valueOf(temp);
        }
        return s;
    }

    /**
     * 有符号16进制数转10进制
     */
    public static String hex2Dec(String hex){
        String s = "";
        if(hex.length() <= 2){
            if(Integer.valueOf(hex,16).shortValue() > 127){
                s = String.valueOf(Integer.valueOf(hex,16).shortValue() - 256);
            }else{
                s = String.valueOf(Integer.valueOf(hex,16).shortValue());
            };
        }else{
            if(Integer.valueOf(hex,16).shortValue() > 32767){
                s = String.valueOf(Integer.valueOf(hex,16).shortValue() - 32767);
            }else{
                s = String.valueOf(Integer.valueOf(hex,16).shortValue());
            };
        }
        return s;
    }
}
