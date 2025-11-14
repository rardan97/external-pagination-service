package com.blackcode.external_pagination_service.config;

import com.blackcode.external_pagination_service.model.User;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${cache.users.expire-after-write-minutes}")
    private long expireAfterWriteMinutes;

    @Value("${cache.users.maximum-size}")
    private long maximumSize;

    @Bean
    public Cache<String, List<User>> userCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .build();
    }

}
