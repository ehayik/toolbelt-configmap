package com.eljaiek.machinery.configuration.core;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface PropertiesBag {

  void put(Property property);

  Property put(String key, String value);

  boolean isEmpty();

  void save();

  void delete(String key);

  void clear();

  void flush();

  void forEach(Consumer<Property> propertyConsumer);

  default <T> Optional<T> getValue(String key, @NonNull Function<String, T> as) {
    return get(key).map(p -> p.map(as)).orElseThrow();
  }

  default Set<Property> getAll(@NonNull Set<String> keys) {
    return keys.stream()
        .map(this::get)
        .map(x -> x.orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors2.toSet());
  }

  Optional<Property> get(String key);

  default Optional<String> getValue(String key) {
    return get(key).flatMap(Property::value);
  }

  default int getValueAsInt(String key) {
    return get(key).map(Property::asInt).orElseThrow();
  }

  default float getValueAsFloat(String key) {
    return get(key).map(Property::asFloat).orElseThrow();
  }

  default long getValueAsLong(String key) {
    return get(key).map(Property::asLong).orElseThrow();
  }

  default List<String> getValueAsList(String key) {
    return get(key).map(Property::asList).orElseGet(List::of);
  }

  default List<String> getValueAsList(String key, @NonNull Supplier<String> splitBy) {
    return get(key).map(p -> p.asList(splitBy)).orElseGet(List::of);
  }

  default <T> List<T> asList(
      String key, Function<String, T> as, @NonNull Supplier<String> splitBy) {
    return get(key).map(p -> p.asList(as, splitBy)).orElseGet(List::of);
  }
}
