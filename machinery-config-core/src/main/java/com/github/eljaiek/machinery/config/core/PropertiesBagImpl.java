package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

final class PropertiesBagImpl implements PropertiesBag {

  private Map<String, Property> bag;
  private final PropertyFactory propertyFactory;

  public PropertiesBagImpl(@NonNull PropertyFactory propertyFactory) {
    this(UnifiedMap.newMap(), propertyFactory);
  }

  PropertiesBagImpl(@NonNull Map<String, String> properties, PropertyFactory propertyFactory) {
    this.bag =
        properties.entrySet().stream().collect(toMap(Map.Entry::getKey, propertyFactory::create));
    this.propertyFactory = propertyFactory;
  }

  public PropertiesBagImpl(
      @NonNull Set<Property> properties, @NonNull PropertyFactory propertyFactory) {
    this.bag = properties.stream().collect(toMap(Property::key, p -> p));
    this.propertyFactory = propertyFactory;
  }

  @Override
  public void put(@NonNull Property property) {
    bag.put(property.key(), property);
  }

  @Override
  public Property put(String key, String value) {
    var property = propertyFactory.create(key, value);
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
  public Optional<Property> remove(String key) {
    return Optional.ofNullable(bag.remove(key));
  }

  @Override
  public void clear() {
    bag = UnifiedMap.newMap();
  }

  @Override
  public void flush() {
    bag.values().forEach(Property::save);
    clear();
  }

  @Override
  public Stream<Property> stream() {
    return bag.values().stream();
  }

  @Override
  public Set<String> keys() {
    return bag.keySet();
  }

  @Override
  public Property get(String key) {
    return bag.get(key);
  }
}
