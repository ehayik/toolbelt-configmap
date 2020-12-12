package com.github.eljaiek.machinery.config.jpa;

import com.github.eljaiek.machinery.config.core.PropertyRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@Configuration
@EnableJpaRepositories
class JpaRepositoryConfiguration {

  @Bean
  @ConditionalOnMissingBean
  PropertyRepository propertyRepository(PropertyEntityRepository propertyEntityRepository) {
    return new JpaPropertyRepository(propertyEntityRepository);
  }
}
