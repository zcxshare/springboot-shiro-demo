package com.zcx.demo.shiroweb.shiro.cache;

import com.zcx.demo.shiroweb.utils.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K,V> implements Cache<K,V> {
    private static final String CACHE_PREFIX = "cache:";

    @Autowired
    private RedisUtil redisUtil;

    private String getKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return CACHE_PREFIX + key;
    }

    @Override
    public V get(K o) throws CacheException {
        if (o == null){
            return null;
        }
        String key = getKey(o.toString());
        return (V) redisUtil.get(key);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        if (k == null || v == null){
            return null;
        }
        String key = getKey(k.toString());
        redisUtil.set(key,v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        if (k == null){
            return null;
        }
        String key = getKey(k.toString());
        V bytes = (V) redisUtil.get(key);
        redisUtil.delete(key);
        return bytes;
    }


    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }


}
