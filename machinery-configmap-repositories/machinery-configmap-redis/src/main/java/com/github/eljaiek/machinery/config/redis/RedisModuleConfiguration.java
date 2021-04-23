package com.github.eljaiek.machinery.config.redis;

import com.github.eljaiek.machinery.config.core.ConfigEntryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
class RedisModuleConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ConfigEntryRepository configEntryRepository(
            ConfigEntryHashRepository configEntryHashRepository) {
        return new RedisConfigEntryRepository(configEntryHashRepository);
    }
}
