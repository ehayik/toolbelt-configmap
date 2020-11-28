package com.github.eljaiek.machinery.configuration.core;

import java.util.Map;

public interface PropertyRepository {

  String getValue(String key);

  Map<String, String> findAllByNamespace(String namespace);

  void save(String key, String value);

  void save(Map<String, String> properties);

  void remove(String key);

  void removeAllByNameSpace(String namespace);
}
