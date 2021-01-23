package com.github.eljaiek.machinery.config.core;

import java.util.Optional;

public interface MutablePropertiesBag extends PropertiesBag<MutableProperty> {

  void save();

  Optional<MutableProperty> remove(String key);

  void clear();

  void flush();
}
