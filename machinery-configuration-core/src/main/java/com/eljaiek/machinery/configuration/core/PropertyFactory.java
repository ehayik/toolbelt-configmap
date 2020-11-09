package com.eljaiek.machinery.configuration.core;

import java.util.Map;

public interface PropertyFactory {

  default Property create(Map.Entry<String, String> entry) {
    return create(entry.getKey(), entry.getValue());
  }

  Property create(String key, String value);
}
