package com.github.ehayik.toolbelt.configmap;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.eclipse.collections.impl.collector.Collectors2.makeString;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@TestMethodOrder(MethodName.class)
class ConfigEntryTests {

    static final String KEY = "time.unit";
    static final String VALUE = DAYS.toString();

    ConfigEntry configEntry;

    @BeforeEach
    void setUp() {
        configEntry = new ConfigEntry(KEY, VALUE);
    }

    @Test
    void saveShouldThrowUnsupportedOperationExceptionWhenEntryIsReadonly() {
        assertThatThrownBy(configEntry::save)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(format("Entry %s is readonly", KEY));
    }

    @Test
    void asTextShouldNotReturnEmpty() {
        assertThat(configEntry.asText()).isEqualTo(VALUE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void asTextShouldReturnEmptyString(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asText()).isEmpty();
    }

    @Test
    void asIntShouldNotReturnZero() {
        // Given
        var entry = configEntry.withValue("80");

        // Then
        assertThat(entry.asInt()).isEqualTo(80);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void asIntShouldReturnZero(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asInt()).isZero();
    }

    @Test
    void asLongShouldNotReturnZero() {
        // Given
        var entry = configEntry.withValue("8000");

        // Then
        assertThat(entry.asLong()).isEqualTo(8000L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void asLongShouldReturnZero(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asLong()).isZero();
    }

    @Test
    void asFloatShouldNotReturnZero() {
        // Given
        var entry = configEntry.withValue("0.5");

        // Then
        assertThat(entry.asFloat()).isEqualTo(0.5F);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void asFloatShouldReturnZero(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asFloat()).isEqualTo(0.0F);
    }

    @ParameterizedTest
    @ValueSource(strings = {"True", "80"})
    void asBooleanShouldReturnTrue(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asBoolean()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"0", "  "})
    void asBooleanShouldReturnFalse(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asBoolean()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.0", "12", "12.004", "-1"})
    void isNumberShouldReturnTrue(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.isNumeric()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "TRUE", "12FE", "ER12.004", "FDefer*"})
    void isNumberShouldReturnFalse(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.isNumeric()).isFalse();
    }

    @Test
    void mapShouldNotReturnEmpty() {
        assertThat(configEntry.map(TimeUnit::valueOf)).isPresent().get().isEqualTo(DAYS);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void mapShouldReturnEmpty(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.map(TimeUnit::valueOf)).isEmpty();
    }

    @Test
    void asListShouldNotReturnEmptyList() {
        // Given
        var timeUnits = TimeUnit.values();
        var value = Stream.of(timeUnits).map(TimeUnit::toString).collect(makeString(" "));
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asList(TimeUnit::valueOf)).contains(timeUnits);
    }

    @Test
    void asListShouldReturnListWithOnlyOneElement() {
        assertThat(configEntry.asList(TimeUnit::valueOf)).hasSize(1);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void asListShouldReturnEmptyList(String value) {
        // Given
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asList(String::toUpperCase)).isEmpty();
    }

    @Test
    void asListShouldReturnNotReturnEmptyListUsingCustomSplitSeparator() {
        // Given
        var timeUnits = TimeUnit.values();
        var value = Stream.of(TimeUnit.values()).map(TimeUnit::toString).collect(makeString(","));
        var entry = configEntry.withValue(value);

        // Then
        assertThat(entry.asList(TimeUnit::valueOf, ",")).contains(timeUnits);
    }

    @Test
    void toJsonShouldReturnEntryAsWellFormedJsonString() {
        assertThat(configEntry.toJson()).isEqualTo("{\"%s\":\"%s\"}", KEY, VALUE);
    }

    @Test
    void hasKeyShouldReturnTrue() {
        assertThat(configEntry.hasKey(KEY)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void hasKeyShouldReturnFalse(String otherKey) {
        assertThat(configEntry.hasKey(otherKey)).isFalse();
    }

    @Test
    void hasValueShouldReturnTrue() {
        assertThat(configEntry.hasValue(VALUE)).isTrue();
    }

    @Test
    void hasValueShouldReturnTrueWhenValuesAreNull() {
        // Given
        var entry = configEntry.withValue(null);

        // Then
        assertThat(entry.hasValue(null)).isTrue();
    }

    @Test
    void hasValueShouldReturnFalse() {
        // Given
        var entry = configEntry.withValue("");

        // Then
        assertThat(entry.hasValue(VALUE)).isFalse();
    }
}
