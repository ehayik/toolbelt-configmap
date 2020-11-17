package com.eljaiek.machinery.configuration.core;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.impl.collector.Collectors2;

@RequiredArgsConstructor
public final class MutablePropertiesBagFactory implements PropertiesBagFactory {

  private final PropertyFactory propertyFactory;
  private final Consumer<Set<Property>> saveBatch;
  private final Supplier<Set<Property>> findAllByNamespace;
  private final Consumer<String> removeAllByNameSpace;

  @Override
  public PropertiesBag create() {
    return new ExtendedMutablePropertyBag(new MutablePropertiesBag(propertyFactory), saveBatch);
  }

  @Override
  public PropertiesBag create(String namespace) {

    if (namespace == null || namespace.isBlank()) {
      throw new IllegalArgumentException("namespace cannot be null or blank");
    }

    return new ExtendedMutablePropertyBag(
        new MutablePropertiesBag(findAllByNamespace, propertyFactory),
        saveBatch,
        () -> removeAllByNameSpace.accept(namespace));
  }

  @Override
  public PropertiesBag create(@NonNull Set<Property> properties) {
    return new ExtendedMutablePropertyBag(
        new MutablePropertiesBag(properties, propertyFactory), saveBatch);
  }

  @Override
  public PropertiesBag create(Map<String, String> properties) {
    return create(
        properties.entrySet().stream().map(propertyFactory::create).collect(Collectors2.toSet()));
  }
}
