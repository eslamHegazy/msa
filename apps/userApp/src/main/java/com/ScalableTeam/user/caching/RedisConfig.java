package com.ScalableTeam.user.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {
    private final String url;
    private final int port;

    public RedisConfig(@Value("${spring.redis.host}") String url,
                                  @Value("${spring.redis.port}") int port) {
        this.url = url;
        this.port = port;
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        log.info("Redis cache running on host: {}, port: {}", url, port);
        return new RedisStandaloneConfiguration(url, port);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisStandaloneConfiguration());
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> empTemplate = new RedisTemplate<>();
        empTemplate.setConnectionFactory(redisConnectionFactory());
        empTemplate.setKeySerializer(new StringRedisSerializer());
        return empTemplate;
    }

}
