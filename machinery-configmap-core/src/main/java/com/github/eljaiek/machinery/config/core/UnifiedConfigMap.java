package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

class UnifiedConfigMap implements ConfigMap {

    private Map<String, ConfigEntry> configEntries;
    private final ConfigEntryFactory configEntryFactory;

    public UnifiedConfigMap(@NonNull ConfigEntryFactory configEntryFactory) {
        this(UnifiedMap.newMap(), configEntryFactory);
    }

    UnifiedConfigMap(
            @NonNull Map<String, String> properties, ConfigEntryFactory configEntryFactory) {
        this.configEntries =
                properties.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, configEntryFactory::create));
        this.configEntryFactory = configEntryFactory;
    }

    public UnifiedConfigMap(
            @NonNull Set<ConfigEntry> properties, @NonNull ConfigEntryFactory configEntryFactory) {
        this.configEntries = properties.stream().collect(toMap(ConfigEntry::key, entry -> entry));
        this.configEntryFactory = configEntryFactory;
    }

    @Override
    public void put(@NonNull ConfigEntry property) {
        configEntries.put(property.key(), property);
    }

    @Override
    public ConfigEntry put(String key, String value) {
        var configEntry = configEntryFactory.create(key, value);
        configEntries.put(key, configEntry);
        return configEntry;
    }

    @Override
    public int size() {
        return configEntries.size();
    }

    @Override
    public boolean isEmpty() {
        return configEntries.isEmpty();
    }

    @Override
    public Optional<ConfigEntry> remove(String key) {
        return Optional.ofNullable(configEntries.remove(key));
    }

    @Override
    public void clear() {
        configEntries = UnifiedMap.newMap();
    }

    @Override
    public void flush() {
        configEntries.values().forEach(ConfigEntry::save);
        clear();
    }

    @Override
    public Stream<ConfigEntry> stream() {
        return configEntries.values().stream();
    }

    @Override
    public Set<String> keys() {
        return configEntries.keySet();
    }

    @Override
    public ConfigEntry get(String key) {
        return configEntries.get(key);
    }
}
