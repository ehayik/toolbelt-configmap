package com.github.ehayik.toolbelt.configmap.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import com.github.ehayik.toolbelt.configmap.ConfigMaps;
import com.github.ehayik.toolbelt.configmap.ConfigSource;
import com.github.ehayik.toolbelt.configmap.SystemConfigSource;
import com.github.ehayik.toolbelt.configmap.jackson.ConfigMapModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
class ConfigMapAutoConfiguration {

    @Bean
    @SuppressWarnings("unused")
    ConfigMaps configMaps(ConfigSource configSource) {
        return new ConfigMaps(configSource);
    }

    @Bean
    @ConditionalOnMissingBean
    ConfigSource configSource() {
        return new SystemConfigSource();
    }

    @Configuration
    @ComponentScan("com.github.ehayik.toolbelt.configmap.sources.jpa")
    @ConditionalOnClass(
            name = "com.github.ehayik.toolbelt.configmap.sources.jpa.JpaModuleConfiguration")
    static class EnableJpaModule {}

    @Configuration
    @ComponentScan("com.github.ehayik.toolbelt.configmap.sources.redis")
    @ConditionalOnClass(
            name = "com.github.ehayik.toolbelt.configmap.sources.redis.RedisModuleConfiguration")
    static class EnableRedisModule {}

    @ConditionalOnWebApplication
    @ConditionalOnClass(name = "com.github.ehayik.toolbelt.configmap.jackson.ConfigMapModule")
    static class InjectJacksonDataTypeModule {

        @Bean
        Module configMapModule(ConfigMaps configMaps) {
            return new ConfigMapModule(configMaps);
        }
    }
}
