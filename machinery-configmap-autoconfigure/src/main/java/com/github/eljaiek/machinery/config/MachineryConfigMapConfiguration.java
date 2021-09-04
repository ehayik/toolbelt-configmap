package com.github.eljaiek.machinery.config;

import com.fasterxml.jackson.databind.Module;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import com.github.eljaiek.machinery.config.core.ConfigSource;
import com.github.eljaiek.machinery.config.jackson.ConfigMapModule;
import com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
class MachineryConfigMapConfiguration {

    @Bean
    @SuppressWarnings("unused")
    ConfigMaps configMaps(ConfigSource configSource) {
        return new ConfigMaps(configSource);
    }

    @Configuration
    @Import(JpaModuleConfiguration.class)
    @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration")
    static class EnableJpaModule {}

    @Configuration
    @ComponentScan("com.github.eljaiek.machinery.config.redis")
    @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.redis.RedisModuleConfiguration")
    static class EnableRedisModule {}

    @ConditionalOnWebApplication
    @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jackson.ConfigMapModule")
    static class InjectJacksonDataTypeModule {

        @Bean
        Module configMapModule(ConfigMaps configMaps) {
            return new ConfigMapModule(configMaps);
        }
    }
}
