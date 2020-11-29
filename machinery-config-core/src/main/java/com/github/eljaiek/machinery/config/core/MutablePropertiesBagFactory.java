package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MutablePropertiesBagFactory implements PropertiesBagFactory {

  private final PropertyRepository propertyRepository;
  private final PropertyFactory propertyFactory;

  @Override
  public PropertiesBag create() {
    return new ExtendedMutablePropertiesBag(new MutablePropertiesBag(propertyFactory), saveBatch());
  }

  private Consumer<Set<Property>> saveBatch() {
    return x -> propertyRepository.save(x.stream().collect(toMap(Property::key, Property::asText)));
  }

  @Override
  public PropertiesBag create(String namespace) {

    if (namespace == null || namespace.isBlank()) {
      throw new IllegalArgumentException("namespace cannot be null or blank");
    }

    var delegate =
        new MutablePropertiesBag(propertyRepository.findAllByNamespace(namespace), propertyFactory);
    return new ExtendedMutablePropertiesBag(
        delegate, saveBatch(), () -> propertyRepository.removeAllByNameSpace(namespace));
  }

  @Override
  public PropertiesBag create(@NonNull Set<Property> properties) {
    return new ExtendedMutablePropertiesBag(
        new MutablePropertiesBag(properties, propertyFactory), saveBatch());
  }

  @Override
  public PropertiesBag create(Map<String, String> properties) {
    return create(properties.entrySet().stream().map(propertyFactory::create).collect(toSet()));
  }
}
