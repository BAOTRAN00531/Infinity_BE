package com.example.infinityweb_be.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCache suggest = new CaffeineCache("lexicon_suggest",
                Caffeine.newBuilder()
                        .maximumSize(5000)
                        .expireAfterWrite(Duration.ofDays(7))
                        .build());
        SimpleCacheManager m = new SimpleCacheManager();
        m.setCaches(List.of(suggest));
        return m;
    }
}