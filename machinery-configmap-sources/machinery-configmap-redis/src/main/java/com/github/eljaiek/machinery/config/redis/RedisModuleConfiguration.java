package com.github.eljaiek.machinery.config.redis;

import com.github.eljaiek.machinery.config.core.ConfigSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
class RedisModuleConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ConfigSource redisConfigSource(ConfigEntryHashRepository configEntryHashRepository) {
        return new RedisConfigSource(configEntryHashRepository);
    }
}
