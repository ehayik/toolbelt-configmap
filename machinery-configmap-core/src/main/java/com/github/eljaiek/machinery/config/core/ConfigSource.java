package com.github.eljaiek.machinery.config.core;

import java.util.Map;

public interface ConfigSource {

    String getValue(String key);

    Map<String, String> groupBy(String prefix);

    void save(String key, String value);

    void save(Map<String, String> configEntries);
}
