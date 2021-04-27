package com.github.eljaiek.machinery.config.core;

import static lombok.AccessLevel.PACKAGE;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

class TransientConfigMap implements ConfigMap {

    private final ConfigMap delegate;

    @Getter(PACKAGE)
    private final Set<String> transientEntryKeys;

    private final Consumer<Set<ConfigEntry>> saveEntries;

    public TransientConfigMap(
            @NonNull ConfigMap delegate,
            @NonNull Set<String> transientEntryKeys,
            @NonNull Consumer<Set<ConfigEntry>> saveEntries) {

        if (delegate instanceof TransientConfigMap) {
            throw new IllegalArgumentException("delegate cannot be instance of TransientConfigMap");
        }

        this.delegate = delegate;
        this.transientEntryKeys = transientEntryKeys;
        this.saveEntries = saveEntries;
    }

    public TransientConfigMap(
            @NonNull ConfigMap delegate, @NonNull Consumer<Set<ConfigEntry>> saveEntries) {
        this(delegate, UnifiedSet.newSet(), saveEntries);
    }

    boolean isTransient(String entryKey) {
        return transientEntryKeys.contains(entryKey);
    }

    boolean hasTransientEntries() {
        return !transientEntryKeys.isEmpty();
    }

    @Override
    public void put(ConfigEntry configEntry) {
        delegate.put(configEntry);
        transientEntryKeys.add(configEntry.key());
    }

    @Override
    public ConfigEntry put(String key, String value) {
        var entry = delegate.put(key, value);
        transientEntryKeys.add(key);
        return entry;
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
        transientEntryKeys.clear();
        delegate.clear();
    }

    @Override
    public <T> Optional<T> getValueAs(String key, @NonNull Function<String, T> as) {
        return delegate.getValueAs(key, as);
    }

    @Override
    public Set<ConfigEntry> getAll(@NonNull Set<String> keys) {
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
        saveEntries.accept(delegate.getAll(transientEntryKeys));
        clear();
    }

    @Override
    public Stream<ConfigEntry> stream() {
        return delegate.stream();
    }

    @Override
    public Set<String> keys() {
        return delegate.keys();
    }

    @Override
    public void forEach(Consumer<ConfigEntry> consumer) {
        delegate.forEach(consumer);
    }

    @Override
    public ConfigEntry get(String key) {
        return delegate.get(key);
    }

    @Override
    public Optional<ConfigEntry> remove(String key) {
        transientEntryKeys.removeIf(x -> x.equals(key));
        return delegate.remove(key);
    }
}
