package com.bigdata.bigdata.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 值 【1】
     * @param timeUnit   单位 【时，天，秒】
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public boolean expire(String key, Long expireTime, TimeUnit timeUnit) {
        try {
            redisTemplate.expire(key, expireTime, timeUnit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String getString(final String key) {
        String result = null;
        ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * key自增
     * @param key
     */
    public void incrby(final String key,Long expireTime, TimeUnit timeUnit ){
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.increment(key,1);
        redisTemplate.expire(key, expireTime, timeUnit);
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 哈希获取所有键值
     * @param key
     * @return
     */
    public Set<Object> hmKeys(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);
    }

    /**
     * 哈希删除
     * @param key
     * @param hashKeys
     * @return
     */
    public Long hmDels(String key, String... hashKeys) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.delete(key,hashKeys);
    }

    /**
     * 哈希长度
     * @param key
     * @return
     */
    public Long hmLens(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.size(key);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public long lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.rightPush(k, v);
    }

    /**
     * 列表获取从第一条开始
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 获取列表指定位置
     * @param k
     * @param index
     * @return
     */
    public String lIndex(String k, Long index) {
        ListOperations<String, String> list = redisTemplate.opsForList();
        return list.index(k, index);
    }

    /**
     * 列表长度
     * @param k
     * @return
     */
    public Long lLen(String k) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.size(k);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public Long add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public Boolean zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.add(key, value, scoure);
    }

    /**
     * 有序集合根据score获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 有序集合根据索引获取
     *
     * @param key
     * @param start 0
     * @param end   -1
     * @return Set<Object>
     */
    public Set<Object> range(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.range(key, start, end);
    }

    /**
     * 有序集合分页查询获取
     *
     * @param key    键
     * @param min    最小score
     * @param max    最大score
     * @param offset 页数mix
     * @param count  条数
     * @return Set<Object>
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 有序集合查看zSet集合的成员个数
     *
     * @param key 键
     * @return Long
     */
    public Long zCard(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.zCard(key);
    }

}