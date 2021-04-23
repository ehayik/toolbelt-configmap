package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConfigMapFactoryImpl implements ConfigMapFactory {

    private final ConfigEntryRepository configEntryRepository;
    private final ConfigEntryFactory configEntryFactory;

    @Override
    public ConfigMap create() {
        return new TransientConfigMap(new UnifiedConfigMap(configEntryFactory), saveBatch());
    }

    private Consumer<Set<ConfigEntry>> saveBatch() {
        return x ->
                configEntryRepository.save(
                        x.stream().collect(toMap(ConfigEntry::key, ConfigEntry::asText)));
    }

    @Override
    public ConfigMap create(String namespace) {

        if (namespace == null || namespace.isBlank()) {
            throw new IllegalArgumentException("namespace cannot be null or blank");
        }

        var delegate =
                new UnifiedConfigMap(
                        configEntryRepository.findAllByNamespace(namespace), configEntryFactory);

        return new TransientConfigMap(delegate, saveBatch());
    }

    @Override
    public ConfigMap create(@NonNull Set<ConfigEntry> entries) {
        var delegate = new UnifiedConfigMap(entries, configEntryFactory);
        return new TransientConfigMap(delegate, delegate.keys(), saveBatch());
    }

    @Override
    public ConfigMap create(Map<String, String> configEntries) {
        return create(
                configEntries.entrySet().stream().map(configEntryFactory::create).collect(toSet()));
    }
}
