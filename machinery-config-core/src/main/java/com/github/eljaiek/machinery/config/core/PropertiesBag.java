package com.github.eljaiek.machinery.config.core;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface PropertiesBag {

  void put(Property property);

  Property put(String key, String value);

  Property get(String key);

  boolean isEmpty();

  int size();

  void save();

  Optional<Property> remove(String key);

  void clear();

  void flush();

  Stream<Property> stream();

  default void forEach(@NonNull Consumer<Property> propertyConsumer) {
    stream().forEach(propertyConsumer);
  }

  default <T> Optional<T> getValueAs(String key, @NonNull Function<String, T> as) {
    return Optional.ofNullable(get(key)).flatMap(p -> p.map(as));
  }

  default Set<Property> getAll(@NonNull Set<String> keys) {
    return keys.stream().map(this::get).filter(Objects::nonNull).collect(Collectors2.toSet());
  }

  default Optional<String> getValue(String key) {
    var property = get(key);

    if (property == null) {
      return Optional.empty();
    }

    return get(key).value();
  }

  default String getValueAsText(String key) {
    var property = get(key);

    if (property == null) {
      return "";
    }

    return get(key).asText();
  }

  default int getValueAsInt(String key) {
    var property = get(key);

    if (property == null) {
      return 0;
    }

    return get(key).asInt();
  }

  default float getValueAsFloat(String key) {
    var property = get(key);

    if (property == null) {
      return 0.0F;
    }

    return get(key).asFloat();
  }

  default long getValueAsLong(String key) {
    var property = get(key);

    if (property == null) {
      return 0L;
    }
    return get(key).asLong();
  }

  default List<String> getValueAsList(String key) {
    var property = get(key);

    if (property == null) {
      return List.of();
    }

    return get(key).asList();
  }

  default List<String> getValueAsList(String key, @NonNull String splitRegex) {
    var property = get(key);

    if (property == null) {
      return List.of();
    }

    return get(key).asList(splitRegex);
  }

  default <T> List<T> getValueAsList(String key, @NonNull Function<String, T> as) {
    var property = get(key);

    if (property == null) {
      return List.of();
    }

    return get(key).asList(as);
  }

  default <T> List<T> getValueAsList(
      String key, Function<String, T> as, @NonNull String splitRegex) {
    var property = get(key);

    if (property == null) {
      return List.of();
    }

    return get(key).asList(as, splitRegex);
  }
}
