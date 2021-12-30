package com.example.rssfeedanalyzer.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * User: pelesic
 */
@Profile("!integration")
@Configuration
@EnableCaching
@EnableConfigurationProperties
public class RssFeedAnalyzerCacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties cacheProperties) {
        CaffeineCache messageCache = buildCache("feeditems", cacheProperties.getFeeditems().getSecondsToLive(),
                cacheProperties.getFeeditems().getMaxEntries());
        CaffeineCache notificationCache = buildCache("restfeed",
                cacheProperties.getRestfeed().getSecondsToLive(), cacheProperties.getRestfeed().getMaxEntries());
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(messageCache, notificationCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, int secondsToExpire, int maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(secondsToExpire, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build());
    }
}
