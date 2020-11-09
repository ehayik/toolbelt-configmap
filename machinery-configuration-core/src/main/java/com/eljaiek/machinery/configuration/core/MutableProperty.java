package com.eljaiek.machinery.configuration.core;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "key")
@ToString(of = {"key", "value"})
public final class MutableProperty implements Property {

  private final String key;
  private final String value;
  private final Consumer<Property> save;
  private final Consumer<String> deleteByKey;

  @Override
  public String key() {
    return value;
  }

  @Override
  public Optional<String> value() {
    return Optional.ofNullable(value);
  }

  @Override
  public void save() {
    save.accept(this);
  }

  @Override
  public void delete() {
    deleteByKey.accept(key);
  }
}
