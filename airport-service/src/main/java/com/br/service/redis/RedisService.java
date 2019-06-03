package com.br.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务
 *
 * @Author Zero
 * @Date 2019 02 22
 */
@Service("redisService")
public class RedisService {

    // RedisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存字段缓存到 Redis
     *
     * @param key   键
     * @param value 值
     */
    public void saveCacheOfFields(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取单个字段缓存
     *
     * @param key 键
     * @return Object
     */
    public Object getCacheOfFields(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    /**
     * 保存字段缓存到 Redis Hash
     *
     * @param hashName Hash 名称
     * @param value    值
     */
    public void saveCacheOfHash(String hashName, String key, Object value) {
        this.redisTemplate.opsForHash().put(hashName, key, value);
    }

    /**
     * 保存字段缓存到 Redis Hash
     *
     * @param hashName Hash 名称
     * @param map      HashMap
     */
    public void saveCacheOfHash(String hashName, Map<String, Object> map) {
        this.redisTemplate.opsForHash().putAll(hashName, map);
    }

    /**
     * 获取缓存从Hash中
     *
     * @param hashName Hash 名称
     * @return Map
     */
    public Map getCacheOfHash(String hashName) {
        return this.redisTemplate.opsForHash().entries(hashName);
    }

    /**
     * 获取缓存从Hash中
     *
     * @param hashName Hash 名称
     * @return Map
     */
    public Object getCacheOfHash(String hashName, String key) {
        return this.redisTemplate.opsForHash().get(hashName, key);
    }

    /**
     * 判断缓存是否存在Hash中
     *
     * @param hashName Hash名称
     * @param key      键值
     * @return
     */
    public boolean hasCacheOfHash(String hashName, Object key) {
        return this.redisTemplate.opsForHash().hasKey(hashName, key);
    }


    /**
     * 从Hash中删除指定缓存
     *
     * @param hashName Hash名称
     * @param key      键
     * @return 删除成功的值
     */
    public void removeCacheOfHash(String hashName, Object key) {
        if (this.hasCacheOfHash(hashName, key)) {
            this.redisTemplate.opsForHash().delete(hashName, key);
        }
    }


    /**
     * 保存字段缓存到 Redis List
     *
     * @param listName
     * @param list     List
     */
    public void saveCacheOfList(String listName, List list) {
        this.redisTemplate.opsForList().leftPushAll(listName, list);
    }

    /**
     * 获取缓存从List中
     *
     * @param listName List 名称
     * @return
     */
    public List getCacheOfList(String listName) {
        return this.redisTemplate.opsForList().range(listName, 0, -1);
    }


    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param time
     */
    public void setCacheExpire(String key, Long time) {
        this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

}
