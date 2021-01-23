package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import java.util.Set;

public interface MutablePropertiesBagFactory {

  MutablePropertiesBag create();

  MutablePropertiesBag create(String namespace);

  MutablePropertiesBag create(Set<MutableProperty> properties);

  MutablePropertiesBag create(Map<String, String> properties);
}
