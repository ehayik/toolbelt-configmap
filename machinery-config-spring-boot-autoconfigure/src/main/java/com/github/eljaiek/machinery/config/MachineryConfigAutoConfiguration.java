package com.github.eljaiek.machinery.config;

import com.github.eljaiek.machinery.config.core.ImmutablePropertyFactory;
import com.github.eljaiek.machinery.config.core.MutablePropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertiesBagFactory;
import com.github.eljaiek.machinery.config.core.PropertyFactory;
import com.github.eljaiek.machinery.config.core.PropertyRepository;
import com.github.eljaiek.machinery.config.jpa.JpaPropertyRepository;
import com.github.eljaiek.machinery.config.jpa.PropertyEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
  @EntityScan("com.github.eljaiek.machinery.config.jpa")
  @EnableJpaRepositories("com.github.eljaiek.machinery.config.jpa")
  @ConditionalOnClass(name = "com.github.eljaiek.machinery.config.jpa.JpaPropertyRepository")
  static class EnableConfigJpa {

    @Bean
    @ConditionalOnMissingBean
    PropertyRepository propertyRepository(PropertyEntityRepository propertyEntityRepository) {
      return new JpaPropertyRepository(propertyEntityRepository);
    }
  }
}
