package com.br.utils;

import java.util.Map;
import java.util.TreeMap;

public class SortUtils {
    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return Map
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                (String s1, String s2) -> s2.compareToIgnoreCase(s1));
        sortMap.putAll(map);
        return sortMap;
    }
}
