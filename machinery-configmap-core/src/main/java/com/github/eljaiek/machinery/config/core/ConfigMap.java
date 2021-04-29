package com.github.eljaiek.machinery.config.core;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface ConfigMap {

    void put(ConfigEntry configEntry);

    ConfigEntry put(String key, String value);

    ConfigEntry get(String key);

    boolean isEmpty();

    int size();

    Stream<ConfigEntry> stream();

    Optional<ConfigEntry> remove(String key);

    void save();

    void clear();

    default Set<String> keys() {
        return stream().map(ConfigEntry::key).collect(Collectors2.toSet());
    }

    default void forEach(@NonNull Consumer<ConfigEntry> propertyConsumer) {
        stream().forEach(propertyConsumer);
    }

    default <S> Optional<S> getValueAs(String key, @NonNull Function<String, S> as) {
        return ofNullable(get(key)).flatMap(p -> p.map(as));
    }

    default Set<ConfigEntry> entries() {
        return stream().collect(toSet());
    }

    default Set<ConfigEntry> entries(@NonNull Set<String> keys) {
        return keys.stream().map(this::get).filter(Objects::nonNull).collect(Collectors2.toSet());
    }

    default Optional<String> getValue(String key) {
        return ofNullable(get(key)).flatMap(ConfigEntry::value);
    }

    default String getValueAsText(String key) {
        return ofNullable(get(key)).map(ConfigEntry::asText).orElse("");
    }

    default int getValueAsInt(String key) {
        return ofNullable(get(key)).map(ConfigEntry::asInt).orElse(0);
    }

    default float getValueAsFloat(String key) {
        return ofNullable(get(key)).map(ConfigEntry::asFloat).orElse(0.0F);
    }

    default long getValueAsLong(String key) {
        return ofNullable(get(key)).map(ConfigEntry::asLong).orElse(0L);
    }

    default List<String> getValueAsList(String key) {
        return ofNullable(get(key)).map(ConfigEntry::asList).orElseGet(List::of);
    }

    default List<String> getValueAsList(String key, @NonNull String splitRegex) {
        return ofNullable(get(key)).map(p -> p.asList(splitRegex)).orElseGet(List::of);
    }

    default <S> List<S> getValueAsList(String key, @NonNull Function<String, S> as) {
        return ofNullable(get(key)).map(p -> p.asList(as)).orElseGet(List::of);
    }

    default <S> List<S> getValueAsList(
            String key, Function<String, S> as, @NonNull String splitRegex) {
        return ofNullable(get(key)).map(p -> p.asList(as, splitRegex)).orElseGet(List::of);
    }

    String toJson();
}
