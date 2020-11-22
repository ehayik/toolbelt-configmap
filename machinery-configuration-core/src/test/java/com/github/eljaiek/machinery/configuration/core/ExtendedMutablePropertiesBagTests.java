package com.github.eljaiek.machinery.configuration.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import refutils.ReflectionHelper;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(Alphanumeric.class)
class ExtendedMutablePropertiesBagTests {

  final String key = "time.unit";
  final String value = DAYS.toString();

  @Mock Property property;
  @Mock Consumer<Set<Property>> saveBatch;
  @Mock PropertiesBag delegateBag;
  ExtendedMutablePropertiesBag propertyBag;

  @BeforeEach
  void setUp() {
    propertyBag = new ExtendedMutablePropertiesBag(delegateBag, saveBatch);
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
  @SuppressWarnings("unchecked")
  void saveShouldCallSaveBatchConsumer() {
    // Given
    Set<Property> properties = Set.of(this.property);
    Set<String> transientPropertyKeys =
        (Set<String>) new ReflectionHelper(propertyBag).getField(Set.class);

    // When
    when(property.key()).thenReturn(key);
    when(delegateBag.getAll(transientPropertyKeys)).thenReturn(properties);
    propertyBag.put(this.property);
    propertyBag.save();

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
    verify(delegateBag).flush();
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
