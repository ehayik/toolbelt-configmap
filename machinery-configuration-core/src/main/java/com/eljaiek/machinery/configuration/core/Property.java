package com.eljaiek.machinery.configuration.core;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface Property {

  String key();

  void save();

  void remove();

  Optional<String> value();

  boolean isNumeric();

  default String asText() {
    return value().filter(x -> !x.isBlank()).orElse("");
  }

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
    return asList(" ");
  }

  default <T> List<T> asList(@NonNull Function<String, T> as, @NonNull String splitRegex) {
    return asList(splitRegex).stream().map(as).collect(Collectors2.toList());
  }

  default List<String> asList(@NonNull String splitRegex) {
    return value()
        .filter(x -> !x.isBlank())
        .map(val -> List.of(val.split(splitRegex)))
        .orElseGet(List::of);
  }
}
