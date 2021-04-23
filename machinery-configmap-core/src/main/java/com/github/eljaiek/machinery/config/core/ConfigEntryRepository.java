package com.github.eljaiek.machinery.config.core;

import java.util.Map;

public interface ConfigEntryRepository {

    String getValue(String key);

    Map<String, String> findAllByNamespace(String namespace);

    void save(String key, String value);

    void save(Map<String, String> configEntries);
}