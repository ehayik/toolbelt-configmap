package com.eljaiek.machinery.configuration.core;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

public final class MutablePropertiesBag implements PropertiesBag {

  private Map<String, Property> bag;
  private final PropertyFactory propertyFactory;

  public MutablePropertiesBag(@NonNull PropertyFactory propertyFactory) {
    this(UnifiedMap.newMap(), propertyFactory);
  }

  MutablePropertiesBag(@NonNull Map<String, String> properties, PropertyFactory propertyFactory) {
    this.bag =
        properties.entrySet().stream()
            .collect(Collectors2.toMap(Map.Entry::getKey, propertyFactory::create));
    this.propertyFactory = propertyFactory;
  }

  public MutablePropertiesBag(
      @NonNull Supplier<Set<Property>> propertiesSupplier,
      @NonNull PropertyFactory propertyFactory) {
    this(propertiesSupplier.get(), propertyFactory);
  }

  public MutablePropertiesBag(
      @NonNull Set<Property> properties, @NonNull PropertyFactory propertyFactory) {
    this.bag = properties.stream().collect(Collectors2.toMap(Property::key, p -> p));
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
  public int size() {
    return bag.size();
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
  public Optional<Property> remove(String key) {
    var property = bag.remove(key);

    if (property == null) {
      return Optional.empty();
    }

    property.remove();
    return Optional.of(property);
  }

  @Override
  public void clear() {
    bag.values().forEach(Property::remove);
    flush();
  }

  @Override
  public void flush() {
    bag = UnifiedMap.newMap();
  }

  @Override
  public Stream<Property> stream() {
    return bag.values().stream();
  }

  @Override
  public Property get(String key) {
    return bag.get(key);
  }
}
