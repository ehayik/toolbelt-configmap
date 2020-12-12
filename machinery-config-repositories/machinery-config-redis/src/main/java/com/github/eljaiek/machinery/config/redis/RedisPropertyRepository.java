package com.github.eljaiek.machinery.config.redis;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import com.github.eljaiek.machinery.config.core.PropertyRepository;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisPropertyRepository implements PropertyRepository {

  private final PropertyHashRepository propertyHashRepository;

  @Override
  public String getValue(String key) {
    return propertyHashRepository.findById(key).map(PropertyHash::getValue).orElse("");
  }

  @Override
  public Map<String, String> findAllByNamespace(String namespace) {
    return propertyHashRepository.findAllByKeyStartingWith(namespace).stream()
        .collect(toMap(PropertyHash::getKey, PropertyHash::getValue));
  }

  @Override
  public void save(String key, String value) {
    propertyHashRepository.save(new PropertyHash(key, value));
  }

  @Override
  public void save(Map<String, String> properties) {
    Set<PropertyHash> propertyHashes =
        properties.entrySet().stream()
            .map(x -> new PropertyHash(x.getKey(), x.getValue()))
            .collect(toSet());
    propertyHashRepository.saveAll(propertyHashes);
  }

  @Override
  public void remove(String key) {
    propertyHashRepository.deleteById(key);
  }

  @Override
  public void removeAllByNameSpace(String namespace) {
    propertyHashRepository.deleteAllByKeyStartingWith(namespace);
  }
}
