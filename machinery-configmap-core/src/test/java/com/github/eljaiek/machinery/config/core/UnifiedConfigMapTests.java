package com.github.eljaiek.machinery.config.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.collector.Collectors2.makeString;
import static org.eclipse.collections.impl.collector.Collectors2.toList;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodName.class)
class UnifiedConfigMapTests {

    static final String KEY = "time.unit";
    static final String OTHER_KEY = "other.prop";
    static final String VALUE = DAYS.toString();

    ConfigEntry configEntry;

    @BeforeEach
    void setUp() {
        configEntry = new ConfigEntry(KEY, VALUE);
    }

    @Test
    void putShouldAddPropertyAndNotReturnNull() {
        // Given
        var configMap = new UnifiedConfigMap();
        var entry = configMap.put(KEY, VALUE);

        // Then
        assertThat(entry).isNotNull();
        assertThat(configMap.get(entry.key())).isNotNull().isEqualTo(entry);
    }

    @Test
    void isEmptyShouldReturnFalse() {
        assertThat(new UnifiedConfigMap(Set.of(configEntry)).isEmpty()).isFalse();
    }

    @Test
    void removeShouldReturnRemovedProperty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        var actual = configMap.remove(KEY);

        // Then
        assertThat(actual).isPresent();
        assertThat(configMap.isEmpty()).isTrue();
    }

    @Test
    void removeShouldReturnEmptyWhenPropertyIsNotPresent() {
        // Given
        var configMap = new UnifiedConfigMap();

        // When
        var actual = configMap.remove(KEY);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void clearShouldSucceed() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        configMap.clear();

        // Then
        assertThat(configMap.isEmpty()).isTrue();
    }

    @Test
    void forEachShouldIterateProperties() {
        // Given
        var values = new LinkedList<String>();
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        configMap.forEach(x -> values.add(x.asText()));

        // Then
        assertThat(values).containsOnly(VALUE);
    }

    @Test
    void getValueAsShouldNotReturnEmpty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAs(KEY, TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
    }

    @Test
    void getValueAsShouldReturnEmpty() {
        // Given
        var entry = configEntry.withValue("");
        var configMap = new UnifiedConfigMap(Set.of(entry));

        // Then
        assertThat(configMap.getValueAs(KEY, TimeUnit::valueOf)).isEmpty();
    }

    @Test
    void getAllShouldNotReturnEmptyList() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getAll(Set.of(KEY, OTHER_KEY))).containsOnly(configEntry);
    }

    @Test
    void getAllShouldReturnEmptyList() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getAll(Set.of(OTHER_KEY))).isEmpty();
    }

    @Test
    void getValueShouldNotReturnEmpty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValue(KEY)).isPresent();
    }

    @Test
    void getValueShouldReturnEmpty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValue(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsTextShouldNotReturnEmpty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsText(KEY)).isEqualTo(VALUE);
    }

    @Test
    void getValueAsTextShouldReturnEmpty() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsText(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsIntShouldNotReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry.withValue("2")));

        // Then
        assertThat(configMap.getValueAsInt(KEY)).isEqualTo(2);
    }

    @Test
    void getValueAsIntShouldReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsInt(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsFloatShouldNotReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry.withValue("2.0")));

        // Then
        assertThat(configMap.getValueAsFloat(KEY)).isEqualTo(2.0F);
    }

    @Test
    void getValueAsFloatShouldReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsFloat(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsLongShouldNotReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry.withValue("80000")));

        // Then
        assertThat(configMap.getValueAsLong(KEY)).isEqualTo(80000L);
    }

    @Test
    void getValueAsLongShouldReturnZero() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsLong(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyList() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        var configMap =
                new UnifiedConfigMap(
                        Set.of(configEntry.withValue(values.stream().collect(makeString(" ")))));

        // Then
        assertThat(configMap.getValueAsList(KEY)).contains(values.toArray(new String[0]));
    }

    @Test
    void getValueAsListShouldReturnEmptyList() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparator() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        var configMap =
                new UnifiedConfigMap(
                        Set.of(configEntry.withValue(values.stream().collect(makeString(",")))));

        // Then
        assertThat(configMap.getValueAsList(KEY, ",")).contains(values.toArray(String[]::new));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparator() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, ",")).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingMapAsFunction() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        Function<String, String> as = String::toLowerCase;
        var configMap =
                new UnifiedConfigMap(
                        Set.of(configEntry.withValue(values.stream().collect(makeString(" ")))));

        // Then
        assertThat(configMap.getValueAsList(KEY, as))
                .contains(values.stream().map(as).toArray(String[]::new));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingMapAsFunction() {
        // Given
        Function<String, String> as = String::toLowerCase;
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, as)).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        Function<String, String> as = String::toLowerCase;
        var configMap =
                new UnifiedConfigMap(
                        Set.of(configEntry.withValue(values.stream().collect(makeString(",")))));

        // Then
        assertThat(configMap.getValueAsList(KEY, as, ","))
                .contains(values.stream().map(as).toArray(String[]::new));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
        // Given
        Function<String, String> as = String::toLowerCase;
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, as, ",")).isEmpty();
    }

    @Test
    void toJsonShouldReturnConfigMapAsWellFormedJsonString() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.toJson()).isEqualTo("[{\"%s\":\"%s\"}]", KEY, VALUE);
    }
}
