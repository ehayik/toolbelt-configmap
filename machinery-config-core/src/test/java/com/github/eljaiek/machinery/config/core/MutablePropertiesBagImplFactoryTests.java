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
class MutablePropertiesBagImplFactoryTests {

  final String key = "time.unit";
  final String value = DAYS.toString();

  @Mock MutableProperty property;
  @Mock PropertyRepository propertyRepository;
  MutablePropertiesBagFactoryImpl factory;
  @Mock MutablePropertyFactory mutablePropertyFactory;

  InstanceOfAssertFactory<MutablePropertiesBag, ObjectAssert<MutablePropertiesBag>>
      mutablePropertiesBagAssertFactory;

  @BeforeEach
  @SuppressWarnings({"rawtypes", "unchecked"})
  void setUp() {
    factory = new MutablePropertiesBagFactoryImpl(propertyRepository, mutablePropertyFactory);
    mutablePropertiesBagAssertFactory =
        new InstanceOfAssertFactory(PropertiesBag.class, Assertions::assertThat);
  }

  @Test
  void createShouldReturnEmptyPropertiesBag() {
    // When
    var propertiesBag = factory.create();

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBagImpl.class);
    assertThat(propertiesBag.isEmpty()).isTrue();
  }

  @Test
  @SuppressWarnings("unchecked")
  void createShouldReturnPropertiesBagWithFetchedPropertiesByNamespace() {
    // When
    when(propertyRepository.findAllByNamespace("time")).thenReturn(Map.of(key, value));
    when(mutablePropertyFactory.create(any(Entry.class))).thenReturn(property);
    var propertiesBag = factory.create("time");

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBagImpl.class);
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
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBagImpl.class);
    assertThat(propertiesBag.isEmpty()).isFalse();
  }

  @Test
  @SuppressWarnings("unchecked")
  void createShouldReturnPropertiesBagContainingGivenPropertiesMap() {
    // When
    when(property.key()).thenReturn(key);
    when(mutablePropertyFactory.create(any(Entry.class))).thenReturn(property);
    var propertiesBag = factory.create(Map.of(key, value));

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBagImpl.class);
    assertThat(propertiesBag.isEmpty()).isFalse();
  }
}
