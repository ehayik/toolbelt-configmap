package com.github.eljaiek.machinery.config.core;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class UnifiedConfigMapTests {

    static final String KEY = "time.unit";
    static final String OTHER_KEY = "other.prop";
    static final String VALUE = DAYS.toString();
    static final BiConsumer<String, String> SAVE = (x, y) -> {};

    @Mock ConfigEntry configEntry;

    @Test
    void putShouldAddPropertyAndNotReturnNull() {
        // Given
        var configMap = new UnifiedConfigMap();
        var configEntry = configMap.put(KEY, VALUE);

        // Then
        assertThat(configEntry).isNotNull();
        assertThat(configMap.get(configEntry.key())).isNotNull().isEqualTo(configEntry);
    }

    @Test
    void isEmptyShouldReturnFalse() {
        assertThat(new UnifiedConfigMap(Set.of(configEntry)).isEmpty()).isFalse();
    }

    @Test
    void saveShouldSucceed() {
        // Given
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        configMap.save();

        // Then
        verify(configEntry).save();
        assertThat(configMap.isEmpty()).isTrue();
    }

    @Test
    void removeShouldReturnRemovedProperty() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
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
        when(configEntry.key()).thenReturn(KEY);
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
        when(configEntry.asText()).thenReturn(VALUE);
        configMap.forEach(x -> values.add(x.asText()));

        // Then
        assertThat(values).containsOnly(VALUE);
    }

    @Test
    void getValueAsShouldNotReturnEmpty() {
        // Given
        var configEntry = new ConfigEntry(KEY, VALUE, SAVE);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAs(KEY, TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
    }

    @Test
    void getValueAsShouldReturnEmpty() {
        // Given
        var configEntry = new ConfigEntry(KEY, "", SAVE);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAs(KEY, TimeUnit::valueOf)).isEmpty();
    }

    @Test
    void getAllShouldNotReturnEmptyList() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getAll(Set.of(KEY, OTHER_KEY))).containsOnly(configEntry);
    }

    @Test
    void getAllShouldReturnEmptyList() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getAll(Set.of(OTHER_KEY))).isEmpty();
    }

    @Test
    void getValueShouldNotReturnEmpty() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.value()).thenReturn(Optional.of(VALUE));

        // Then
        assertThat(configMap.getValue(KEY)).isPresent();
    }

    @Test
    void getValueShouldReturnEmpty() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValue(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsTextShouldNotReturnEmpty() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asText()).thenReturn(VALUE);

        // Then
        assertThat(configMap.getValueAsText(KEY)).isEqualTo(VALUE);
    }

    @Test
    void getValueAsTextShouldReturnEmpty() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsText(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsIntShouldNotReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asInt()).thenReturn(2);

        // Then
        assertThat(configMap.getValueAsInt(KEY)).isEqualTo(2);
    }

    @Test
    void getValueAsIntShouldReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsInt(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsFloatShouldNotReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asFloat()).thenReturn(2.0F);

        // Then
        assertThat(configMap.getValueAsFloat(KEY)).isEqualTo(2.0F);
    }

    @Test
    void getValueAsFloatShouldReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsFloat(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsLongShouldNotReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asLong()).thenReturn(80000L);

        // Then
        assertThat(configMap.getValueAsLong(KEY)).isEqualTo(80000L);
    }

    @Test
    void getValueAsLongShouldReturnZero() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsLong(OTHER_KEY)).isZero();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyList() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asList()).thenReturn(values);

        // Then
        assertThat(configMap.getValueAsList(KEY)).contains(values.toArray(new String[0]));
    }

    @Test
    void getValueAsListShouldReturnEmptyList() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY)).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparator() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asList(",")).thenReturn(values);

        // Then
        assertThat(configMap.getValueAsList(KEY, ",")).contains(values.toArray(new String[0]));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparator() {
        // Given
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, ",")).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingMapAsFunction() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        Function<String, String> as = String::toLowerCase;
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asList(as)).thenReturn(values);

        // Then
        assertThat(configMap.getValueAsList(KEY, as)).contains(values.toArray(new String[0]));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingMapAsFunction() {
        // Given
        Function<String, String> as = String::toLowerCase;
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, as)).isEmpty();
    }

    @Test
    void getValueAsListShouldNotReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
        // Given
        var values = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(toList());
        Function<String, String> as = String::toLowerCase;
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // When
        when(configEntry.asList(as, ",")).thenReturn(values);

        // Then
        assertThat(configMap.getValueAsList(KEY, as, ",")).contains(values.toArray(new String[0]));
    }

    @Test
    void getValueAsListShouldReturnEmptyListWhenPassingSplitSeparatorAndMapAsFunction() {
        // Given
        Function<String, String> as = String::toLowerCase;
        when(configEntry.key()).thenReturn(KEY);
        var configMap = new UnifiedConfigMap(Set.of(configEntry));

        // Then
        assertThat(configMap.getValueAsList(OTHER_KEY, as, ",")).isEmpty();
    }
}
