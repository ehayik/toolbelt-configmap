package com.github.eljaiek.machinery.config.jpa;

import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import com.github.eljaiek.machinery.config.core.ConfigEntryRepository;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaConfigEntryRepository implements ConfigEntryRepository {

    private final ConfigEntryEntityRepository delegate;

    @Override
    public String getValue(String key) {
        return delegate.getValue(key);
    }

    @Override
    public Map<String, String> findAllByNamespace(String namespace) {
        return delegate.findAllByKeyStartingWith(namespace).stream()
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
                        .map(x -> new ConfigEntryEntity(x.getKey(), x.getValue()))
                        .collect(toList());
        delegate.saveAll(entities);
    }
}
