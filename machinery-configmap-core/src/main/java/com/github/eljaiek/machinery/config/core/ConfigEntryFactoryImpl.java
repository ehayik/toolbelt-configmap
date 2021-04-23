package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigEntryFactoryImpl implements ConfigEntryFactory {

    private final ConfigEntryRepository configEntryRepository;

    @Override
    public ConfigEntry create(@NonNull Map.Entry<String, String> entry) {
        return create(entry.getKey(), entry.getValue());
    }

    @Override
    public ConfigEntry create(String key, String value) {
        return create(key, () -> value);
    }

    private ConfigEntry create(String key, Supplier<String> value) {

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("ConfigEntry key cannot be null or blank.");
        }

        return new ConfigEntry(
                key, value.get(), p -> configEntryRepository.save(p.key(), p.asText()));
    }

    @Override
    public ConfigEntry create(String key) {
        return create(key, () -> configEntryRepository.getValue(key));
    }
}
