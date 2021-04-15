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
class PropertiesBagImplFactoryTests {

    final String key = "time.unit";
    final String value = DAYS.toString();

    @Mock Property property;
    @Mock PropertyRepository propertyRepository;
    PropertiesBagFactoryImpl factory;
    @Mock PropertyFactory propertyFactory;

    InstanceOfAssertFactory<PropertiesBag, ObjectAssert<PropertiesBag>>
            mutablePropertiesBagAssertFactory;

    @BeforeEach
    @SuppressWarnings({"rawtypes", "unchecked"})
    void setUp() {
        factory = new PropertiesBagFactoryImpl(propertyRepository, propertyFactory);
        mutablePropertiesBagAssertFactory =
                new InstanceOfAssertFactory(PropertiesBag.class, Assertions::assertThat);
    }

    @Test
    void createShouldReturnEmptyPropertiesBag() {
        // When
        var propertiesBag = factory.create();

        // Then
        assertThat(propertiesBag)
                .isInstanceOf(ExtendedPropertiesBag.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(PropertiesBagImpl.class);
        assertThat(propertiesBag.isEmpty()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void createShouldReturnPropertiesBagWithFetchedPropertiesByNamespace() {
        // When
        when(propertyRepository.findAllByNamespace("time")).thenReturn(Map.of(key, value));
        when(propertyFactory.create(any(Entry.class))).thenReturn(property);
        var propertiesBag = factory.create("time");

        // Then
        assertThat(propertiesBag)
                .isInstanceOf(ExtendedPropertiesBag.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(PropertiesBagImpl.class);
        assertThat(propertiesBag.isEmpty()).isFalse();
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
        when(property.key()).thenReturn(key);
        var propertiesBag = factory.create(Set.of(property));

        // Then
        assertThat(propertiesBag)
                .isInstanceOf(ExtendedPropertiesBag.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(PropertiesBagImpl.class);
        assertThat(propertiesBag.isEmpty()).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void createShouldReturnPropertiesBagContainingGivenPropertiesMap() {
        // When
        when(property.key()).thenReturn(key);
        when(propertyFactory.create(any(Entry.class))).thenReturn(property);
        var propertiesBag = factory.create(Map.of(key, value));

        // Then
        assertThat(propertiesBag)
                .isInstanceOf(ExtendedPropertiesBag.class)
                .extracting("delegate", mutablePropertiesBagAssertFactory)
                .isInstanceOf(PropertiesBagImpl.class);
        assertThat(propertiesBag.isEmpty()).isFalse();
    }
}
