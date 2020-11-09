package com.eljaiek.machinery.configuration.core;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.NonNull;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

public final class MutablePropertiesBag implements PropertiesBag {

  private Map<String, Property> bag;
  private final PropertyFactory propertyFactory;

  public MutablePropertiesBag(@NonNull PropertyFactory propertyFactory) {
    this(UnifiedMap.newMap(), propertyFactory);
  }

  public MutablePropertiesBag(@NonNull Map<String, Property> bag, PropertyFactory propertyFactory) {
    this.bag = bag;
    this.propertyFactory = propertyFactory;
  }

  @Override
  public void put(@NonNull Property property) {
    bag.put(property.key(), property);
  }

  @Override
  public Property put(String key, String value) {
    Property property = propertyFactory.create(key, value);
    bag.put(key, property);
    return property;
  }

  @Override
  public boolean isEmpty() {
    return bag.isEmpty();
  }

  @Override
  public void save() {
    bag.values().forEach(Property::save);
  }

  @Override
  public void delete(String key) {
    get(key).ifPresent(Property::delete);
    bag.remove(key);
  }

  @Override
  public void clear() {
    bag.values().forEach(Property::delete);
    flush();
  }

  @Override
  public void flush() {
    bag = UnifiedMap.newMap();
  }

  @Override
  public void forEach(@NonNull Consumer<Property> consumer) {
    bag.values().forEach(consumer);
  }

  @Override
  public Optional<Property> get(String key) {
    return Optional.ofNullable(bag.get(key));
  }
}
