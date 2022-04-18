package io.piral.feedservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String PILET_FILE_CACHE_NAME = "piletFileCache";
}
