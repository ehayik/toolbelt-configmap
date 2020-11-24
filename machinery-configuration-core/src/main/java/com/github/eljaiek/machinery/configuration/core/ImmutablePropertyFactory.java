package com.github.eljaiek.machinery.configuration.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public final class ImmutablePropertyFactory implements PropertyFactory {

  private final PropertyRepository propertyRepository;

  @Override
  public Property create(@NonNull Map.Entry<String, String> entry) {
    return create(entry.getKey(), entry.getValue());
  }

  @Override
  public Property create(String key, String value) {

    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("key cannot be null or blank.");
    }

    return new ImmutableProperty(key, value, propertyRepository::put, propertyRepository::remove);
  }
}
