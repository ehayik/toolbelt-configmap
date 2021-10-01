package com.github.ehayik.toolbelt.configmap.sources.redis;

import com.github.ehayik.toolbelt.configmap.ConfigSource;
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
