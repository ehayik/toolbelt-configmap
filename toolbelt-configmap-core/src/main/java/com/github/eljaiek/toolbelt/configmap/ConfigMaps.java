package com.github.eljaiek.toolbelt.configmap;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConfigMaps {

    private final ConfigSource configSource;

    public ConfigMap groupBy(String prefix) {

        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("prefix cannot be null or blank");
        }

        var delegate = new UnifiedConfigMap(configSource.groupBy(prefix), this::of);
        return new TransientConfigMap(delegate, saveEntries());
    }

    private Consumer<Set<ConfigEntry>> saveEntries() {
        return x ->
                configSource.save(x.stream().collect(toMap(ConfigEntry::key, ConfigEntry::asText)));
    }

    public ConfigMap of() {
        return new TransientConfigMap(new UnifiedConfigMap(this::of), saveEntries());
    }

    public ConfigEntry of(String key) {
        return new ConfigEntry(key, configSource.getValue(key), configSource::save);
    }

    public ConfigMap of(@NonNull Map<String, String> configEntries) {
        return of(configEntries.entrySet().stream().map(this::of).collect(toSet()));
    }

    private ConfigEntry of(Entry<String, String> entry) {
        return new ConfigEntry(entry.getKey(), entry.getValue(), configSource::save);
    }

    public ConfigMap of(@NonNull Set<ConfigEntry> entries) {
        var delegate = new UnifiedConfigMap(entries, this::of);
        return new TransientConfigMap(delegate, delegate.keys(), saveEntries());
    }

    public ConfigEntry of(String key, String value) {
        return new ConfigEntry(key, value, configSource::save);
    }
}
