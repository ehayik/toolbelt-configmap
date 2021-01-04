package com.github.eljaiek.machinery.config;

import com.github.eljaiek.machinery.config.core.ImmutablePropertyFactory;
import com.github.eljaiek.machinery.config.core.MutablePropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertyFactory;
import com.github.eljaiek.machinery.config.core.PropertyRepository;
import com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
class MachineryConfigAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  PropertyFactory propertyFactory(PropertyRepository propertyRepository) {
    return new ImmutablePropertyFactory(propertyRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  PropertiesBagFactory propertiesBagFactory(
      PropertyFactory propertyFactory, PropertyRepository propertyRepository) {
    return new MutablePropertiesBagFactory(propertyRepository, propertyFactory);
  }

  @Configuration
  @Import(JpaModuleConfiguration.class)
  @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jpa.JpaModuleConfiguration")
  static class EnableJpaModule {}

  @Configuration
  @ComponentScan("com.github.eljaiek.machinery.config.redis.RedisModuleConfiguration")
  @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.redis.RedisModuleConfiguration")
  static class EnableRedisModule {}
}
