package com.github.eljaiek.machinery.config.core;

import lombok.NonNull;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static org.eclipse.collections.impl.collector.Collectors2.makeString;
import static org.eclipse.collections.impl.collector.Collectors2.toMap;

class UnifiedConfigMap implements ConfigMap {

    private Map<String, ConfigEntry> configEntries;
    private final BiFunction<String, String, ConfigEntry> entryBuilder;

    UnifiedConfigMap() {
        this(ConfigEntry::new);
    }

    UnifiedConfigMap(BiFunction<String, String, ConfigEntry> entryBuilder) {
        this(UnifiedMap.newMap(), entryBuilder);
    }

    UnifiedConfigMap(
            @NonNull Map<String, String> configEntries,
            @NonNull BiFunction<String, String, ConfigEntry> entryBuilder) {
        this.entryBuilder = entryBuilder;
        this.configEntries =
                configEntries.entrySet().stream()
                        .collect(
                                toMap(
                                        Entry::getKey,
                                        x -> entryBuilder.apply(x.getKey(), x.getValue())));
    }

    UnifiedConfigMap(Set<ConfigEntry> configEntries) {
        this(configEntries, ConfigEntry::new);
    }

    UnifiedConfigMap(
            @NonNull Set<ConfigEntry> configEntries,
            @NonNull BiFunction<String, String, ConfigEntry> entryBuilder) {
        this.configEntries =
                configEntries.stream().collect(toMap(ConfigEntry::key, entry -> entry));
        this.entryBuilder = entryBuilder;
    }

    @Override
    public void put(@NonNull ConfigEntry property) {
        configEntries.put(property.key(), property);
    }

    @Override
    public ConfigEntry put(String key, String value) {
        var configEntry = entryBuilder.apply(key, value);
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
    public void save() {
        configEntries.values().forEach(ConfigEntry::save);
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
    public String toJson() {
        return stream() //
                .map(ConfigEntry::toJson) //
                .collect(makeString("[", ",", "]"));
    }

    @Override
    public ConfigEntry get(String key) {
        return configEntries.get(key);
    }
}
