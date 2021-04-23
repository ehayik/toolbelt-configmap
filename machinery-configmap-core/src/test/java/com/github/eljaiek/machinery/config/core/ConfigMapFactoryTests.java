package com.github.eljaiek.machinery.config.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class ConfigMapFactoryTests {

    static final String KEY = "time.unit";
    static final String VALUE = DAYS.toString();

    @Mock ConfigEntry configEntry;
    @Mock ConfigEntryRepository configEntryRepository;
    @Mock ConfigEntryFactory configEntryFactory;
    ConfigMapFactoryImpl factory;

    InstanceOfAssertFactory<ConfigMap, ObjectAssert<ConfigMap>> mutablePropertiesBagAssertFactory;

    @BeforeEach
    @SuppressWarnings({"rawtypes", "unchecked"})
    void setUp() {
        factory = new ConfigMapFactoryImpl(configEntryRepository, configEntryFactory);
        mutablePropertiesBagAssertFactory =
                new InstanceOfAssertFactory(ConfigMap.class, Assertions::assertThat);
    }

    @Test
    void createShouldReturnEmptyPropertiesBag() {
        // When
        var configMap = factory.create();

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void createShouldReturnPropertiesBagWithFetchedPropertiesByNamespace() {
        // When
        when(configEntryRepository.findAllByNamespace("time")).thenReturn(Map.of(KEY, VALUE));
        when(configEntryFactory.create(any(Entry.class))).thenReturn(configEntry);
        var configMap = factory.create("time");

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void createShouldThrowIllegalArgumentExceptionWhenNamespaceIsBlank(String namespace) {
        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> factory.create(namespace))
                .withMessage("namespace cannot be null or blank");
    }

    @Test
    void createShouldReturnPropertiesBagContainingGivenPropertiesSet() {
        // When
        when(configEntry.key()).thenReturn(KEY);
        var configMap = factory.create(Set.of(configEntry));

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void createShouldReturnPropertiesBagContainingGivenPropertiesMap() {
        // When
        when(configEntry.key()).thenReturn(KEY);
        when(configEntryFactory.create(any(Entry.class))).thenReturn(configEntry);
        var configMap = factory.create(Map.of(KEY, VALUE));

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }
}
