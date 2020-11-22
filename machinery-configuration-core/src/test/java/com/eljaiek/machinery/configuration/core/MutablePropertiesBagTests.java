package com.eljaiek.machinery.configuration.core;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(Alphanumeric.class)
class MutablePropertiesBagTests {

  final String key = "time.unit";
  final String otherKey = "other.prop";
  final String value = DAYS.toString();

  @Mock Property property;
  @Mock PropertyFactory propertyFactory;

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
    var property = propertiesBag.put(key, value);

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
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    var actual = propertiesBag.remove(key);

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
    var actual = propertiesBag.remove(key);

    // Then
    assertThat(actual).isEmpty();
  }

  @Test
  void clearShouldSucceed() {
    // Given
    when(property.key()).thenReturn(key);
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
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asText()).thenReturn(value);
    propertiesBag.forEach(x -> values.add(x.asText()));

    // Then
    assertThat(values).containsOnly(value);
  }

  @Test
  void getValueAsShouldNotReturnEmpty() {
    // Given
    var immutableProperty = new ImmutableProperty(key, value, s -> {}, s -> {});
    var propertiesBag = new MutablePropertiesBag(Set.of(immutableProperty), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAs(key, TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
  }

  @Test
  void getValueAsShouldReturnEmpty() {
    // Given
    var immutableProperty = new ImmutableProperty(key, "", s -> {}, s -> {});
    var propertiesBag = new MutablePropertiesBag(Set.of(immutableProperty), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAs(key, TimeUnit::valueOf)).isEmpty();
  }

  @Test
  void getAllShouldNotReturnEmptyList() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getAll(Set.of(key, otherKey))).containsOnly(property);
  }

  @Test
  void getAllShouldReturnEmptyList() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getAll(Set.of(otherKey))).isEmpty();
  }

  @Test
  void getValueShouldNotReturnEmpty() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.value()).thenReturn(Optional.of(value));

    // Then
    assertThat(propertiesBag.getValue(key)).isPresent();
  }

  @Test
  void getValueShouldReturnEmpty() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValue(otherKey)).isEmpty();
  }

  @Test
  void getValueAsTextShouldNotReturnEmpty() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asText()).thenReturn(value);

    // Then
    assertThat(propertiesBag.getValueAsText(key)).isEqualTo(value);
  }

  @Test
  void getValueAsTextShouldReturnEmpty() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsText(otherKey)).isEmpty();
  }

  @Test
  void getValueAsIntShouldNotReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asInt()).thenReturn(2);

    // Then
    assertThat(propertiesBag.getValueAsInt(key)).isEqualTo(2);
  }

  @Test
  void getValueAsIntShouldReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsInt(otherKey)).isZero();
  }

  @Test
  void getValueAsFloatShouldNotReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asFloat()).thenReturn(2.0F);

    // Then
    assertThat(propertiesBag.getValueAsFloat(key)).isEqualTo(2.0F);
  }

  @Test
  void getValueAsFloatShouldReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsFloat(otherKey)).isZero();
  }

  @Test
  void getValueAsLongShouldNotReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asLong()).thenReturn(80000L);

    // Then
    assertThat(propertiesBag.getValueAsLong(key)).isEqualTo(80000L);
  }

  @Test
  void getValueAsLongShouldReturnZero() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsLong(otherKey)).isZero();
  }

  @Test
  void getValueAsListShouldNotReturnEmptyList() {
    // Given
    var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asList()).thenReturn(values);

    // Then
    assertThat(propertiesBag.getValueAsList(key)).contains(values.toArray(new String[0]));
  }

  @Test
  void getValueAsListShouldReturnEmptyList() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsList(otherKey)).isEmpty();
  }

  @Test
  void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparator() {
    // Given
    var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asList(",")).thenReturn(values);

    // Then
    assertThat(propertiesBag.getValueAsList(key, ",")).contains(values.toArray(new String[0]));
  }

  @Test
  void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparator() {
    // Given
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsList(otherKey, ",")).isEmpty();
  }

  @Test
  void getValueAsListShouldNotReturnEmptyListWhenPassingMapAsFunction() {
    // Given
    var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
    Function<String, String> as = String::toLowerCase;
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asList(as)).thenReturn(values);

    // Then
    assertThat(propertiesBag.getValueAsList(key, as)).contains(values.toArray(new String[0]));
  }

  @Test
  void getValueAsListShouldReturnEmptyListWhenPassingMapAsFunction() {
    // Given
    Function<String, String> as = String::toLowerCase;
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsList(otherKey, as)).isEmpty();
  }

  @Test
  void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
    // Given
    var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
    Function<String, String> as = String::toLowerCase;
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // When
    when(property.asList(as, ",")).thenReturn(values);

    // Then
    assertThat(propertiesBag.getValueAsList(key, as, ",")).contains(values.toArray(new String[0]));
  }

  @Test
  void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
    // Given
    Function<String, String> as = String::toLowerCase;
    when(property.key()).thenReturn(key);
    var propertiesBag = new MutablePropertiesBag(Set.of(property), propertyFactory);

    // Then
    assertThat(propertiesBag.getValueAsList(otherKey, as, ",")).isEmpty();
  }
}
