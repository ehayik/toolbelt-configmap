package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import java.util.Set;

public interface ConfigMapFactory {

    ConfigMap create();

    ConfigMap create(String namespace);

    ConfigMap create(Set<ConfigEntry> entries);

    ConfigMap create(Map<String, String> configEntries);
}
