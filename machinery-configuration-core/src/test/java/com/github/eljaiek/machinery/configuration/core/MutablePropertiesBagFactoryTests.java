package com.github.eljaiek.machinery.configuration.core;

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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class MutablePropertiesBagFactoryTests {

  String key = "time.unit";
  String value = DAYS.toString();

  @Mock Property property;
  @Mock PropertyRepository propertyRepository;
  MutablePropertiesBagFactory factory;
  @Mock PropertyFactory propertyFactory;
  InstanceOfAssertFactory<PropertiesBag, ObjectAssert<PropertiesBag>>
      mutablePropertiesBagAssertFactory;

  @BeforeEach
  @SuppressWarnings({"rawtypes", "unchecked"})
  void setUp() {
    factory = new MutablePropertiesBagFactory(propertyRepository, propertyFactory);
    mutablePropertiesBagAssertFactory =
        new InstanceOfAssertFactory(PropertiesBag.class, Assertions::assertThat);
  }

  @Test
  void createShouldReturnEmptyPropertiesBag() {
    // When
    PropertiesBag propertiesBag = factory.create();

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBag.class);
    assertThat(propertiesBag.isEmpty()).isTrue();
  }

  @Test
  @SuppressWarnings("unchecked")
  void createShouldReturnPropertiesBagWithFetchedPropertiesByNamespace() {
    // When
    when(propertyRepository.findAllByNamespace("time")).thenReturn(Map.of(key, value));
    when(propertyFactory.create(any(Entry.class))).thenReturn(property);
    PropertiesBag propertiesBag = factory.create("time");

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBag.class);
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
    PropertiesBag propertiesBag = factory.create(Set.of(property));

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBag.class);
    assertThat(propertiesBag.isEmpty()).isFalse();
  }

  @Test
  @SuppressWarnings("unchecked")
  void createShouldReturnPropertiesBagContainingGivenPropertiesMap() {
    // When
    when(property.key()).thenReturn(key);
    when(propertyFactory.create(any(Entry.class))).thenReturn(property);
    PropertiesBag propertiesBag = factory.create(Map.of(key, value));

    // Then
    assertThat(propertiesBag)
        .isInstanceOf(ExtendedMutablePropertiesBag.class)
        .extracting("delegate", mutablePropertiesBagAssertFactory)
        .isInstanceOf(MutablePropertiesBag.class);
    assertThat(propertiesBag.isEmpty()).isFalse();
  }
}
