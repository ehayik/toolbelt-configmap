package com.github.eljaiek.machinery.config.jpa;

import com.github.eljaiek.machinery.config.core.ConfigEntryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@Configuration
@ComponentScan
@EnableJpaRepositories
public class JpaModuleConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ConfigEntryRepository configEntryRepository(
            ConfigEntryEntityRepository configEntryEntityRepository) {
        return new JpaConfigEntryRepository(configEntryEntityRepository);
    }
}
