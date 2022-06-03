package com.ScalableTeam.user.caching;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisUtility {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(final String userId, String authToken) {
        redisTemplate.opsForValue().set(userId, authToken);
        redisTemplate.expire(userId, 30, TimeUnit.DAYS);
    }


    public String getValue(final String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void deleteKeyFromRedis(String userId) {
        redisTemplate.delete(userId);
    }
}
