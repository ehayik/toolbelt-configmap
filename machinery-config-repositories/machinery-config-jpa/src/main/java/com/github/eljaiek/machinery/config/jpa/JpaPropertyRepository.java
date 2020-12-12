package com.github.eljaiek.machinery.config.jpa;

import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import com.github.eljaiek.machinery.config.core.PropertyRepository;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaPropertyRepository implements PropertyRepository {

  private final PropertyEntityRepository delegate;

  @Override
  public String getValue(String key) {
    return delegate.getValue(key);
  }

  @Override
  public Map<String, String> findAllByNamespace(String namespace) {
    return delegate.findAllByKeyStartingWith(namespace).stream()
        .collect(toMap(PropertyEntity::getKey, PropertyEntity::getValue));
  }

  @Override
  public void save(String key, String value) {
    delegate.save(new PropertyEntity(key, value));
  }

  @Override
  public void save(@NonNull Map<String, String> properties) {
    List<PropertyEntity> entities =
        properties.entrySet().stream()
            .map(x -> new PropertyEntity(x.getKey(), x.getValue()))
            .collect(toList());
    delegate.saveAll(entities);
  }

  @Override
  public void remove(@NonNull String key) {
    delegate.deleteById(key);
  }

  @Override
  public void removeAllByNameSpace(@NonNull String namespace) {
    delegate.deleteAllByKeyStartingWith(namespace);
  }
}
