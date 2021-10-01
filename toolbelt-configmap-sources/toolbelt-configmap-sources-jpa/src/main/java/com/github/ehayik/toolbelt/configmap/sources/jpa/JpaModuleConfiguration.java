package com.github.ehayik.toolbelt.configmap.sources.jpa;

import com.github.ehayik.toolbelt.configmap.ConfigSource;
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
    ConfigSource jpaConfigSource(ConfigEntryEntityRepository configEntryEntityRepository) {
        return new JpaConfigSource(configEntryEntityRepository);
    }
}
