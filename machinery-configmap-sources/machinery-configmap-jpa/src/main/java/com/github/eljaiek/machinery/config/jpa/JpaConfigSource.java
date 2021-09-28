package com.github.eljaiek.machinery.config.jpa;

import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import com.github.eljaiek.machinery.config.core.ConfigSource;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaConfigSource implements ConfigSource {

    private final ConfigEntryEntityRepository delegate;

    @Override
    public String getValue(String key) {
        return delegate.getValue(key);
    }

    @Override
    public Map<String, String> groupBy(String prefix) {
        return delegate.findAllByKeyStartingWith(prefix).stream()
                .collect(toMap(ConfigEntryEntity::getKey, ConfigEntryEntity::getValue));
    }

    @Override
    public void save(String key, String value) {
        delegate.save(new ConfigEntryEntity(key, value));
    }

    @Override
    public void save(@NonNull Map<String, String> configEntries) {
        List<ConfigEntryEntity> entities =
                configEntries.entrySet().stream()
                        .map(JpaConfigSource::asConfigEntryEntity)
                        .collect(toList());
        delegate.saveAll(entities);
    }

    private static ConfigEntryEntity asConfigEntryEntity(Entry<String, String> entry) {
        return new ConfigEntryEntity(entry.getKey(), entry.getValue());
    }
}
