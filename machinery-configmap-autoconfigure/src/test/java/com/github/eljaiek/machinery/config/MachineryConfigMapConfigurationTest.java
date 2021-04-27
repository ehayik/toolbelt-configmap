package com.github.eljaiek.machinery.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.eljaiek.machinery.config.core.ConfigMaps;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = MachineryConfigMapConfiguration.class)
class MachineryConfigMapConfigurationTest {

    @Autowired ApplicationContext applicationContext;

    @Test
    void configMapsShouldBePresentWhenContextIsBootstrapped() {
        assertThat(applicationContext.getBean(ConfigMaps.class)).isNotNull();
    }
}
