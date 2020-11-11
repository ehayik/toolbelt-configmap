package com.eljaiek.machinery.configuration.core;

import lombok.NonNull;

import java.util.Map;

public interface PropertyFactory {

  default Property create(@NonNull Map.Entry<String, String> entry) {
    return create(entry.getKey(), entry.getValue());
  }

  Property create(String key, String value);
}
