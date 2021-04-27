package com.github.eljaiek.machinery.config;

import com.github.eljaiek.machinery.config.core.ConfigEntryRepository;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
class MachineryConfigMapConfiguration {

    @Bean
    @SuppressWarnings("unused")
    ConfigMaps configMaps(ConfigEntryRepository configEntryRepository) {
        return new ConfigMaps(configEntryRepository);
    }

    @Configuration
    @Import(JpaModuleConfiguration.class)
    @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration")
    static class EnableJpaModule {}

    @Configuration
    @ComponentScan("com.github.eljaiek.machinery.config.redis")
    @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.redis.RedisModuleConfiguration")
    static class EnableRedisModule {}
}
