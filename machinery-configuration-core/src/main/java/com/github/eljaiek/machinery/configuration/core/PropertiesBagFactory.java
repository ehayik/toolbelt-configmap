package com.github.eljaiek.machinery.configuration.core;

import java.util.Map;
import java.util.Set;

public interface PropertiesBagFactory {

  PropertiesBag create();

  PropertiesBag create(String namespace);

  PropertiesBag create(Set<Property> properties);

  PropertiesBag create(Map<String, String> properties);
}
