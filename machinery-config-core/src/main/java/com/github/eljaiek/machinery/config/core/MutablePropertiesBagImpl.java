package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

public final class MutablePropertiesBagImpl implements MutablePropertiesBag {

  private Map<String, MutableProperty> bag;
  private final MutablePropertyFactory mutablePropertyFactory;

  public MutablePropertiesBagImpl(@NonNull MutablePropertyFactory mutablePropertyFactory) {
    this(UnifiedMap.newMap(), mutablePropertyFactory);
  }

  MutablePropertiesBagImpl(
      @NonNull Map<String, String> properties, MutablePropertyFactory mutablePropertyFactory) {
    this.bag =
        properties.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, mutablePropertyFactory::create));
    this.mutablePropertyFactory = mutablePropertyFactory;
  }

  public MutablePropertiesBagImpl(
      @NonNull Set<MutableProperty> properties,
      @NonNull MutablePropertyFactory mutablePropertyFactory) {
    this.bag = properties.stream().collect(toMap(MutableProperty::key, p -> p));
    this.mutablePropertyFactory = mutablePropertyFactory;
  }

  @Override
  public void put(@NonNull MutableProperty property) {
    bag.put(property.key(), property);
  }

  @Override
  public MutableProperty put(String key, String value) {
    var property = mutablePropertyFactory.create(key, value);
    bag.put(key, property);
    return property;
  }

  @Override
  public int size() {
    return bag.size();
  }

  @Override
  public boolean isEmpty() {
    return bag.isEmpty();
  }

  @Override
  public void save() {
    bag.values().forEach(MutableProperty::save);
  }

  @Override
  public Optional<MutableProperty> remove(String key) {
    var property = Optional.ofNullable(bag.remove(key));
    property.ifPresent(MutableProperty::remove);
    return property;
  }

  @Override
  public void clear() {
    bag.values().forEach(MutableProperty::remove);
    flush();
  }

  @Override
  public void flush() {
    bag = UnifiedMap.newMap();
  }

  @Override
  public Stream<MutableProperty> stream() {
    return bag.values().stream();
  }

  @Override
  public MutableProperty get(String key) {
    return bag.get(key);
  }
}
