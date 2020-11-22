package com.eljaiek.machinery.configuration.core;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "key")
@ToString(of = {"key", "value"})
public final class ImmutableProperty implements Property {

  private static final Pattern NUM_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

  private final String key;
  private final @With String value;
  private final Consumer<Property> save;
  private final Consumer<String> removeByKey;

  @Override
  public String key() {
    return key;
  }

  @Override
  public Optional<String> value() {
    return Optional.ofNullable(value);
  }

  @Override
  public boolean isNumeric() {
    return value != null && !value.isBlank() && NUM_PATTERN.matcher(value).matches();
  }

  @Override
  public void save() {
    save.accept(this);
  }

  @Override
  public void remove() {
    removeByKey.accept(key);
  }
}
