package com.github.eljaiek.machinery.config;

import com.github.eljaiek.machinery.config.core.ConfigEntryFactory;
import com.github.eljaiek.machinery.config.core.ConfigEntryFactoryImpl;
import com.github.eljaiek.machinery.config.core.ConfigEntryRepository;
import com.github.eljaiek.machinery.config.core.ConfigMapFactory;
import com.github.eljaiek.machinery.config.core.ConfigMapFactoryImpl;
import com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
class MachineryConfigMapConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ConfigEntryFactory configEntryFactory(ConfigEntryRepository configEntryRepository) {
        return new ConfigEntryFactoryImpl(configEntryRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    ConfigMapFactory configMapFactory(
            ConfigEntryFactory configEntryFactory, ConfigEntryRepository configEntryRepository) {
        return new ConfigMapFactoryImpl(configEntryRepository, configEntryFactory);
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
