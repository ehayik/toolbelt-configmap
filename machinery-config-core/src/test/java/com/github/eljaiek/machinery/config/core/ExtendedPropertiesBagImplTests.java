package com.github.eljaiek.machinery.config.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class ExtendedPropertiesBagImplTests {

    final String key = "time.unit";
    final String value = DAYS.toString();

    @Mock Property property;
    @Mock Consumer<Set<Property>> saveBatch;
    @Mock PropertiesBag delegateBag;
    ExtendedPropertiesBag propertyBag;

    @BeforeEach
    void setUp() {
        propertyBag = new ExtendedPropertiesBag(delegateBag, saveBatch);
    }

    @Test
    void putShouldReturnProperty() {
        // When
        when(delegateBag.put(key, value)).thenReturn(property);
        var actual = propertyBag.put(key, value);

        // Then
        assertThat(actual).isNotNull();
        assertThat(propertyBag.isTransient(key)).isTrue();
    }

    @Test
    @SneakyThrows
    void flushShouldCallSaveBatchConsumer() {
        // Given
        var properties = Set.of(property);

        // When
        when(property.key()).thenReturn(key);
        when(delegateBag.getAll(propertyBag.getTransientPropertyKeys())).thenReturn(properties);
        propertyBag.put(property);
        propertyBag.flush();

        // Then
        verify(saveBatch).accept(properties);
    }

    @Test
    void clearShouldRemoveAllProperties() {
        // When
        when(property.key()).thenReturn(key);
        propertyBag.put(property);
        propertyBag.clear();

        // Then
        verify(delegateBag).clear();
        assertThat(propertyBag.hasTransientProperties()).isFalse();
    }

    @Test
    void removeShouldReturnProperty() {
        // When
        when(property.key()).thenReturn(key);
        when(delegateBag.remove(key)).thenReturn(Optional.of(property));
        propertyBag.put(property);
        var actual = propertyBag.remove(key);

        // Then
        assertThat(propertyBag.isTransient(key)).isFalse();
        assertThat(actual).isPresent().get().isEqualTo(property);
    }

    @Test
    void removeShouldReturnEmpty() {
        // When
        when(property.key()).thenReturn(key);
        propertyBag.put(property);
        var actual = propertyBag.remove("other");

        // Then
        assertThat(propertyBag.isTransient(key)).isTrue();
        assertThat(actual).isEmpty();
    }
}
