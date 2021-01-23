package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MutablePropertyFactoryImpl implements MutablePropertyFactory {

  private final PropertyRepository propertyRepository;

  @Override
  public MutableProperty create(@NonNull Map.Entry<String, String> entry) {
    return create(entry.getKey(), entry.getValue());
  }

  @Override
  public MutableProperty create(String key, String value) {
    return create(key, () -> value);
  }

  private MutableProperty create(String key, Supplier<String> value) {

    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("key cannot be null or blank.");
    }

    return new MutablePropertyImpl(
        key,
        value.get(),
        p -> propertyRepository.save(p.key(), p.asText()),
        propertyRepository::remove);
  }

  @Override
  public MutableProperty create(String key) {
    return create(key, () -> propertyRepository.getValue(key));
  }
}
