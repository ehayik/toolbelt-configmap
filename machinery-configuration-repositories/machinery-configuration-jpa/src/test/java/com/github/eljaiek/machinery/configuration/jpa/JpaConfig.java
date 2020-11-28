package com.github.eljaiek.machinery.configuration.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@Configuration
@EnableJpaRepositories
class JpaConfig {

  @Bean
  JpaPropertyRepository jpaPropertyRepository(PropertyEntityRepository entityRepository) {
    return new JpaPropertyRepository(entityRepository);
  }
}
