package com.github.eljaiek.machinery.config;

import com.github.eljaiek.machinery.config.core.ImmutablePropertyFactory;
import com.github.eljaiek.machinery.config.core.MutablePropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertyFactory;
import com.github.eljaiek.machinery.config.core.PropertyRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
  @ComponentScan(value = "com.github.eljaiek.machinery.config.jpa")
  @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jpa.JpaRepositoryConfiguration")
  static class EnableJpaRepository {}

  @Configuration
  @ComponentScan("com.github.eljaiek.machinery.config.redis")
  @ConditionalOnClass(
      name = "com.github.eljaiek.machinery.config.redis.RedisRepositoryConfiguration")
  static class EnableRedisRepository {}
}
