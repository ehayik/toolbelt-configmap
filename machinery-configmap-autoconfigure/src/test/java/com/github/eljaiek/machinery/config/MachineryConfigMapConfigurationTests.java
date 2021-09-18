package com.github.eljaiek.machinery.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.Module;
import com.github.eljaiek.machinery.config.MachineryConfigMapConfiguration.InjectJacksonDataTypeModule;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import com.github.eljaiek.machinery.config.core.ConfigSource;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = MachineryConfigMapConfiguration.class)
class MachineryConfigMapConfigurationTests {

    @Autowired ApplicationContext context;
    @Autowired ConfigMaps expectedConfigMaps;
    @Autowired ConfigSource expectedSource;
    @Autowired MachineryConfigMapConfiguration machineryConfigMapConfiguration;

    @Test
    void configMapsJacksonModuleShouldNotBePresentWhenContextIsBootstrapped() {
        assertThatThrownBy(() -> context.getBean(Module.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    void configMapsShouldReturnExpectedInstance() {
        // When
        ConfigMaps configMaps = machineryConfigMapConfiguration.configMaps(expectedSource);
        ConfigSource actualSource =
                (ConfigSource) ReflectionTestUtils.getField(configMaps, "configSource");

        // Then
        assertThat(actualSource).isEqualTo(expectedSource);
    }

    @Test
    void injectJacksonDataTypeModuleShouldReturnExpectedConfigMaps() {
        // Given
        InjectJacksonDataTypeModule config = new InjectJacksonDataTypeModule();

        // When
        Module configMapModule = config.configMapModule(expectedConfigMaps);
        ConfigMaps actualConfigMaps =
                (ConfigMaps) ReflectionTestUtils.getField(configMapModule, "configMaps");

        // Then
        assertThat(actualConfigMaps).isEqualTo(expectedConfigMaps);
    }
}
