package com.zcx.demo.shiroweb.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key, Object value,int timeout) {
        redisTemplate.opsForValue().set(key,value,timeout,TimeUnit.SECONDS);
    }

    public void expire(String key, int timeout) {
        redisTemplate.expire(key,timeout, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Set<String> keys(String prefix) {
        return redisTemplate.keys((prefix+"*"));
    }
}
