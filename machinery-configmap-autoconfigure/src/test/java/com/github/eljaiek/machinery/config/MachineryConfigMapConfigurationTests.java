package com.github.eljaiek.machinery.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.Module;
import com.github.eljaiek.machinery.config.MachineryConfigMapConfiguration.InjectJacksonDataTypeModule;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import com.github.eljaiek.machinery.config.core.ConfigSource;
import com.github.eljaiek.machinery.config.core.SystemConfigSource;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodName.class)
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
        assertThat(actualSource).isInstanceOf(SystemConfigSource.class).isEqualTo(expectedSource);
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
