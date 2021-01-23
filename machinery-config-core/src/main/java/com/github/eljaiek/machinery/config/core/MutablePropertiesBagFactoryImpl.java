package com.github.eljaiek.machinery.config.core;

import static org.eclipse.collections.impl.collector.Collectors2.toMap;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MutablePropertiesBagFactoryImpl implements MutablePropertiesBagFactory {

  private final PropertyRepository propertyRepository;
  private final MutablePropertyFactory mutablePropertyFactory;

  @Override
  public MutablePropertiesBag create() {
    return new ExtendedMutablePropertiesBag(
        new MutablePropertiesBagImpl(mutablePropertyFactory), saveBatch());
  }

  private Consumer<Set<MutableProperty>> saveBatch() {
    return x -> propertyRepository.save(x.stream().collect(toMap(Property::key, Property::asText)));
  }

  @Override
  public MutablePropertiesBag create(String namespace) {

    if (namespace == null || namespace.isBlank()) {
      throw new IllegalArgumentException("namespace cannot be null or blank");
    }

    var delegate =
        new MutablePropertiesBagImpl(
            propertyRepository.findAllByNamespace(namespace), mutablePropertyFactory);
    return new ExtendedMutablePropertiesBag(
        delegate, saveBatch(), () -> propertyRepository.removeAllByNameSpace(namespace));
  }

  @Override
  public MutablePropertiesBag create(@NonNull Set<MutableProperty> properties) {
    return new ExtendedMutablePropertiesBag(
        new MutablePropertiesBagImpl(properties, mutablePropertyFactory), saveBatch());
  }

  @Override
  public MutablePropertiesBag create(Map<String, String> properties) {
    return create(
        properties.entrySet().stream().map(mutablePropertyFactory::create).collect(toSet()));
  }
}
