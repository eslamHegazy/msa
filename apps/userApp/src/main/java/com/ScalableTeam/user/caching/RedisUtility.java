package com.ScalableTeam.user.caching;

import com.ScalableTeam.user.caching.RedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisUtility {
    private final RedisTemplate<String, String> redisTemplate;

    @CachePut(cacheNames = RedisConfig.CACHE_NAME)
    public void setValue(final String userId, String authToken){
        redisTemplate.opsForValue().setIfPresent(userId, authToken);
        redisTemplate.expire(userId, 30, TimeUnit.DAYS);
    }


    @Cacheable(cacheNames = RedisConfig.CACHE_NAME)
    public String getValue(final String userId){
        return redisTemplate.opsForValue().get(userId);
    }

    @CacheEvict(cacheNames = RedisConfig.CACHE_NAME)
    public void deleteKeyFromRedis(String userId){
        redisTemplate.delete(userId);
    }
}
