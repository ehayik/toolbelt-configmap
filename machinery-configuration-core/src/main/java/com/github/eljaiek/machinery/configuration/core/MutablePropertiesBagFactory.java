package com.github.eljaiek.machinery.configuration.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.impl.collector.Collectors2;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public final class MutablePropertiesBagFactory implements PropertiesBagFactory {

  private final PropertyRepository propertyRepository;
  private final PropertyFactory propertyFactory;

  @Override
  public PropertiesBag create() {
    return new ExtendedMutablePropertiesBag(new MutablePropertiesBag(propertyFactory), propertyRepository::put);
  }

  @Override
  public PropertiesBag create(String namespace) {

    if (namespace == null || namespace.isBlank()) {
      throw new IllegalArgumentException("namespace cannot be null or blank");
    }

    var delegate = new MutablePropertiesBag(propertyRepository.getAll(namespace), propertyFactory);
    return new ExtendedMutablePropertiesBag(
        delegate, propertyRepository::put, () -> propertyRepository.removeAll(namespace));
  }

  @Override
  public PropertiesBag create(@NonNull Set<Property> properties) {
    return new ExtendedMutablePropertiesBag(
        new MutablePropertiesBag(properties, propertyFactory), propertyRepository::put);
  }

  @Override
  public PropertiesBag create(Map<String, String> properties) {
    return create(
        properties.entrySet().stream().map(propertyFactory::create).collect(Collectors2.toSet()));
  }
}
