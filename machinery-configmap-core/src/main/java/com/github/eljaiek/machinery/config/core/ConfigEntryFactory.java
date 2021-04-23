package com.github.eljaiek.machinery.config.core;

import java.util.Map;
import lombok.NonNull;

public interface ConfigEntryFactory {

    default ConfigEntry create(@NonNull Map.Entry<String, String> entry) {
        return create(entry.getKey(), entry.getValue());
    }

    ConfigEntry create(String key, String value);

    ConfigEntry create(String key);
}
