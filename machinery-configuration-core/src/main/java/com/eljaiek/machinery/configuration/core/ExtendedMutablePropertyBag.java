package com.eljaiek.machinery.configuration.core;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import lombok.NonNull;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

public final class ExtendedMutablePropertyBag implements PropertiesBag {

  private final PropertiesBag delegate;
  private final Set<String> changedKeys;
  private final Consumer<Set<Property>> saveBatch;
  private final Runnable deleteBatch;

  public ExtendedMutablePropertyBag(PropertiesBag delegate, Consumer<Set<Property>> saveBatch) {
    this(delegate, saveBatch, delegate::clear);
  }

  public ExtendedMutablePropertyBag(
      @NonNull PropertiesBag delegate,
      @NonNull Consumer<Set<Property>> saveBatch,
      @NonNull Runnable deleteBatch) {
    this.delegate = delegate;
    this.saveBatch = saveBatch;
    this.deleteBatch = deleteBatch;
    changedKeys = UnifiedSet.newSet();
  }

  @Override
  public void put(Property property) {
    delegate.put(property);
    changedKeys.add(property.key());
  }

  @Override
  public Property put(String key, String value) {
    var property = delegate.put(key, value);
    changedKeys.add(key);
    return property;
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public void clear() {
    deleteBatch.run();
    changedKeys.clear();
    delegate.flush();
  }

  @Override
  public void flush() {
    delegate.flush();
  }

  @Override
  public void forEach(Consumer<Property> consumer) {
    delegate.forEach(consumer);
  }

  @Override
  public Optional<Property> get(String key) {
    return delegate.get(key);
  }

  @Override
  public void save() {
    saveBatch.accept(getAll(changedKeys));
  }

  @Override
  public void delete(String key) {
    delegate.delete(key);
    changedKeys.removeIf(x -> x.equals(key));
  }
}
