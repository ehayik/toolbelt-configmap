package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import lombok.NonNull;

public interface MutablePropertyFactory {

  default MutableProperty create(@NonNull Map.Entry<String, String> entry) {
    return create(entry.getKey(), entry.getValue());
  }

  MutableProperty create(String key, String value);

  MutableProperty create(String key);
}
