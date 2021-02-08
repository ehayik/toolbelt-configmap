package com.github.eljaiek.machinery.config.core;

import static lombok.AccessLevel.PACKAGE;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

@AllArgsConstructor(access = PACKAGE)
public final class ExtendedMutablePropertiesBag implements MutablePropertiesBag {

  private final MutablePropertiesBag delegate;
  private final Consumer<Set<MutableProperty>> saveBatch;
  private final Runnable removeBatch;
  private Set<String> transientPropertyKeys;

  public ExtendedMutablePropertiesBag(
      MutablePropertiesBag delegate, Consumer<Set<MutableProperty>> saveBatch) {
    this(delegate, saveBatch, delegate::clear, null);
  }

  public ExtendedMutablePropertiesBag(
      @NonNull MutablePropertiesBag delegate,
      @NonNull Consumer<Set<MutableProperty>> saveBatch,
      @NonNull Runnable removeBatch) {
    this(delegate, saveBatch, removeBatch, UnifiedSet.newSet());
  }

  boolean isTransient(String propertyKey) {
    return getTransientPropertyKeys().contains(propertyKey);
  }

  Set<String> getTransientPropertyKeys() {

    if (transientPropertyKeys == null) {
      transientPropertyKeys = delegate.keys();
    }

    return transientPropertyKeys;
  }

  boolean hasTransientProperties() {
    return !getTransientPropertyKeys().isEmpty();
  }

  @Override
  public void put(MutableProperty property) {
    delegate.put(property);
    getTransientPropertyKeys().add(property.key());
  }

  @Override
  public MutableProperty put(String key, String value) {
    var property = delegate.put(key, value);
    getTransientPropertyKeys().add(key);
    return property;
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public void clear() {
    removeBatch.run();
    getTransientPropertyKeys().clear();
    delegate.flush();
  }

  @Override
  public <T> Optional<T> getValueAs(String key, @NonNull Function<String, T> as) {
    return delegate.getValueAs(key, as);
  }

  @Override
  public Set<MutableProperty> getAll(@NonNull Set<String> keys) {
    return delegate.getAll(keys);
  }

  @Override
  public Optional<String> getValue(String key) {
    return delegate.getValue(key);
  }

  @Override
  public String getValueAsText(String key) {
    return delegate.getValueAsText(key);
  }

  @Override
  public int getValueAsInt(String key) {
    return delegate.getValueAsInt(key);
  }

  @Override
  public float getValueAsFloat(String key) {
    return delegate.getValueAsFloat(key);
  }

  @Override
  public long getValueAsLong(String key) {
    return delegate.getValueAsLong(key);
  }

  @Override
  public List<String> getValueAsList(String key) {
    return delegate.getValueAsList(key);
  }

  @Override
  public List<String> getValueAsList(String key, @NonNull String splitRegex) {
    return delegate.getValueAsList(key, splitRegex);
  }

  @Override
  public <T> List<T> getValueAsList(String key, @NonNull Function<String, T> as) {
    return delegate.getValueAsList(key, as);
  }

  @Override
  public <T> List<T> getValueAsList(
      String key, Function<String, T> as, @NonNull String splitRegex) {
    return delegate.getValueAsList(key, as, splitRegex);
  }

  @Override
  public void flush() {
    delegate.flush();
  }

  @Override
  public Stream<MutableProperty> stream() {
    return delegate.stream();
  }

  @Override
  public Set<String> keys() {
    return delegate.keys();
  }

  @Override
  public void forEach(Consumer<MutableProperty> consumer) {
    delegate.forEach(consumer);
  }

  @Override
  public MutableProperty get(String key) {
    return delegate.get(key);
  }

  @Override
  public void save() {
    saveBatch.accept(delegate.getAll(getTransientPropertyKeys()));
  }

  @Override
  public Optional<MutableProperty> remove(String key) {
    getTransientPropertyKeys().removeIf(x -> x.equals(key));
    return delegate.remove(key);
  }
}
