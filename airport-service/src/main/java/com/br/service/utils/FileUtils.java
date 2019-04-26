package com.br.service.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读取地图文件工具类
 * @Author Zero
 * @Date 2019 02 24
 */
public class FileUtils {

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     */
    public byte[] readFile(String filePath) {
        byte[] bytes = null;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath);
            bytes = new byte[is.available()];
            int currentByte = 0;
            int index = 0;
            while ((currentByte = is.read()) != -1) {
                bytes[index] = (byte) currentByte;
                index++;
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
