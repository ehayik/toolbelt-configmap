package com.github.eljaiek.machinery.config.redis;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import com.github.eljaiek.machinery.config.core.ConfigSource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisConfigSource implements ConfigSource {

    private final ConfigEntryHashRepository configEntryHashRepository;

    @Override
    public String getValue(String key) {
        return configEntryHashRepository.findById(key).map(ConfigEntryHash::getValue).orElse("");
    }

    @Override
    public Map<String, String> groupBy(String prefix) {
        return filterAllStartingWith(prefix)
                .collect(toMap(ConfigEntryHash::getKey, ConfigEntryHash::getValue));
    }

    private Stream<ConfigEntryHash> filterAllStartingWith(String prefix) {
        return StreamSupport.stream(configEntryHashRepository.findAll().spliterator(), false)
                .filter(p -> p.getKey().startsWith(prefix));
    }

    @Override
    public void save(String key, String value) {
        configEntryHashRepository.save(new ConfigEntryHash(key, value));
    }

    @Override
    public void save(Map<String, String> configEntries) {
        Set<ConfigEntryHash> propertyHashes =
                configEntries.entrySet().stream()
                        .map(x -> new ConfigEntryHash(x.getKey(), x.getValue()))
                        .collect(toSet());
        configEntryHashRepository.saveAll(propertyHashes);
    }
}
