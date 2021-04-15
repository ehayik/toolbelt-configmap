package com.github.eljaiek.machinery.config.core;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import org.eclipse.collections.impl.collector.Collectors2;

public interface PropertiesBag {

    void put(Property property);

    Property put(String key, String value);

    Property get(String key);

    boolean isEmpty();

    int size();

    Stream<Property> stream();

    Optional<Property> remove(String key);

    void flush();

    void clear();

    default Set<String> keys() {
        return stream().map(Property::key).collect(Collectors2.toSet());
    }

    default void forEach(@NonNull Consumer<Property> propertyConsumer) {
        stream().forEach(propertyConsumer);
    }

    default <S> Optional<S> getValueAs(String key, @NonNull Function<String, S> as) {
        return ofNullable(get(key)).flatMap(p -> p.map(as));
    }

    default Set<Property> getAll(@NonNull Set<String> keys) {
        return keys.stream().map(this::get).filter(Objects::nonNull).collect(Collectors2.toSet());
    }

    default Optional<String> getValue(String key) {
        return ofNullable(get(key)).flatMap(Property::value);
    }

    default String getValueAsText(String key) {
        return ofNullable(get(key)).map(Property::asText).orElse("");
    }

    default int getValueAsInt(String key) {
        return ofNullable(get(key)).map(Property::asInt).orElse(0);
    }

    default float getValueAsFloat(String key) {
        return ofNullable(get(key)).map(Property::asFloat).orElse(0.0F);
    }

    default long getValueAsLong(String key) {
        return ofNullable(get(key)).map(Property::asLong).orElse(0L);
    }

    default List<String> getValueAsList(String key) {
        return ofNullable(get(key)).map(Property::asList).orElse(List.of());
    }

    default List<String> getValueAsList(String key, @NonNull String splitRegex) {
        return ofNullable(get(key)).map(p -> p.asList(splitRegex)).orElse(List.of());
    }

    default <S> List<S> getValueAsList(String key, @NonNull Function<String, S> as) {
        return ofNullable(get(key)).map(p -> p.asList(as)).orElse(List.of());
    }

    default <S> List<S> getValueAsList(
            String key, Function<String, S> as, @NonNull String splitRegex) {
        return ofNullable(get(key)).map(p -> p.asList(as, splitRegex)).orElse(List.of());
    }
}
