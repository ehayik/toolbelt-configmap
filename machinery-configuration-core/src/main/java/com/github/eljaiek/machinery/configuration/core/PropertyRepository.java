package com.github.eljaiek.machinery.configuration.core;

import java.util.Set;

public interface PropertyRepository {

  String getValue(String key);

  Set<Property> getAll(String key);

  void put(Property property);

  void put(Set<Property> properties);

  void remove(String key);

  void removeAll(String key);
}
