package com.eljaiek.machinery.configuration.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(Alphanumeric.class)
class MutablePropertiesBagTests {

  static final String KEY = "time.unit";
  static final String OTHER_KEY = "other.prop";
  static final String VALUE = DAYS.toString();

  @Mock Property property;
  @Mock PropertyFactory propertyFactory;

  @BeforeEach
  void setUp() {}

  @Test
  void putShouldAddPropertyAndNotReturnNull() {
    // When
    doAnswer(
            x -> {
              var key = (String) x.getArgument(0);
              var value = (String) x.getArgument(1);
              return new ImmutableProperty(key, value, s -> {}, s -> {});
            })
        .when(propertyFactory)
        .create(anyString(), anyString());

    // Given
    var propertiesBag = new MutablePropertiesBag(propertyFactory);
    var property = propertiesBag.put(KEY, VALUE);

    // Then
    assertThat(property).isNotNull();
    assertThat(propertiesBag.get(property.key())).isNotNull().isEqualTo(property);
  }

  @Test
  void isEmptyShouldReturnFalse() {
    assertThat(new MutablePropertiesBag(Set.of(property), propertyFactory).isEmpty()).isFalse();
  }

  @Test
  void saveShouldSucceed() {
    // Given
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    propertiesBag.save();
    verify(property).save();
  }

  @Test
  void removeShouldReturnRemovedProperty() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    var actual = propertiesBag.remove(KEY);

    // Then
    verify(property).remove();
    assertThat(actual).isPresent();
    assertThat(propertiesBag.size()).isZero();
  }

  @Test
  void removeShouldReturnEmptyWhenPropertyIsNotPresent() {
    // Given
    var propertiesBag = new MutablePropertiesBag(propertyFactory);

    // When
    var actual = propertiesBag.remove(KEY);

    // Then
    assertThat(actual).isEmpty();
  }

  @Test
  void clearShouldSucceed() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    propertiesBag.clear();

    // Then
    verify(property).remove();
    assertThat(propertiesBag.isEmpty()).isTrue();
  }

  @Test
  void forEachShouldIterateProperties() {
    // Given
    var values = new LinkedList<String>();
    Supplier<Set<Property>> propertiesSupplier = () -> Set.of(property);
    var propertiesBag = new MutablePropertiesBag(propertiesSupplier, propertyFactory);

    // When
    when(property.asText()).thenReturn(VALUE);
    propertiesBag.forEach(x -> values.add(x.asText()));

    // Then
    assertThat(values).containsOnly(VALUE);
  }

  @Test
  void getValueAsShouldNotReturnEmpty() {
    // Given
    var immutableProperty = new ImmutableProperty(KEY, VALUE, s -> {}, s -> {});
    var propertiesBag = new MutablePropertiesBag(Set.of(immutableProperty), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAs(KEY, TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
  }

  @Test
  void getValueAsShouldReturnEmpty() {
    // Given
    var immutableProperty = new ImmutableProperty(KEY, "", s -> {}, s -> {});
    var propertiesBag = new MutablePropertiesBag(Set.of(immutableProperty), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAs(KEY, TimeUnit::valueOf)).isEmpty();
  }

  @Test
  void getAllShouldNotReturnEmptyList() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getAll(Set.of(KEY, OTHER_KEY))).containsOnly(property);
  }

  @Test
  void getAllShouldReturnEmptyList() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getAll(Set.of(OTHER_KEY))).isEmpty();
  }

  @Test
  void getValueShouldNotReturnEmpty() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.value()).thenReturn(Optional.of(VALUE));

    // Then
    assertThat(propertiesBag.getValue(KEY)).isPresent();
  }

  @Test
  void getValueShouldReturnEmpty() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValue(OTHER_KEY)).isEmpty();
  }

  @Test
  void getValueAsTextShouldNotReturnEmpty() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asText()).thenReturn(VALUE);

    // Then
    assertThat(propertiesBag.getValueAsText(KEY)).isEqualTo(VALUE);
  }

  @Test
  void getValueAsTextShouldReturnEmpty() {
    // Given
    when(property.key()).thenReturn(KEY);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsText(OTHER_KEY)).isEmpty();
  }
}
