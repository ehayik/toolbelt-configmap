package com.eljaiek.machinery.configuration.core;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface Property {

  String key();

  void save();

  void delete();

  Optional<String> value();

  boolean isNumeric();

  default int asInt() {
    return map(Integer::parseInt).orElse(0);
  }

  default <T> Optional<T> map(@NonNull Function<String, T> as) {
    return value().filter(x -> !x.isBlank()).map(as);
  }

  default long asLong() {
    return map(Long::parseLong).orElse(0L);
  }

  default float asFloat() {
    return map(Float::parseFloat).orElse(0.0F);
  }

  default boolean asBoolean() {

    if (isNumeric()) {
      return value().filter(x -> !x.equals("0") && !x.equals("0.0")).isPresent();
    }

    return map(Boolean::parseBoolean).orElse(false);
  }

  default <T> List<T> asList(@NonNull Function<String, T> as) {
    return asList().stream().map(as).collect(toList());
  }

  default List<String> asList() {
    return asList(() -> " ");
  }

  default <T> List<T> asList(@NonNull Function<String, T> as, @NonNull Supplier<String> splitBy) {
    return asList(splitBy).stream().map(as).collect(Collectors2.toList());
  }

  default List<String> asList(@NonNull Supplier<String> splitBy) {
    return value()
        .filter(x -> !x.isBlank())
        .map(val -> List.of(val.split(splitBy.get())))
        .orElseGet(List::of);
  }
}
