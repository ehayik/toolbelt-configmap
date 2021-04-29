package com.github.eljaiek.machinery.config.jackson;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.eljaiek.machinery.config.core.ConfigEntry;
import org.junit.jupiter.api.Test;

class ConfigEntryDeserializerTests extends ModuleTestBase {

    static final String KEY = "time.unit";
    static final String VALUE = DAYS.toString();

    @Test
    void deserialize() throws JsonProcessingException {
        // Given
        var json = format("{\"%s\":\"%s\"}", KEY, VALUE);

        // When
        var configEntry = mapperWithModule().readValue(json, ConfigEntry.class);

        // Then
        assertThat(configEntry)
                .hasFieldOrPropertyWithValue("key", KEY)
                .hasFieldOrPropertyWithValue("value", VALUE);
    }

    @Test
    void deserializeShouldFailWhenKeyPropertyIsMissing() {
        // Given
        var json = format("{\"\":\"%s\"}", VALUE);

        // Then
        assertThatThrownBy(() -> mapperWithModule().readValue(json, ConfigEntry.class))
                .isInstanceOf(JsonProcessingException.class)
                .hasMessageContaining(
                        "Cannot construct instance of `com.github.eljaiek.machinery.config.core.ConfigEntry`, problem: key cannot be null or blank.\n"
                                + " at [Source: (String)\"{\"\":\"DAYS\"}\"; line: 1, column: 11]");
    }

    @Test
    void deserializeShouldFailWhenDataIsNotAnObject() {
        // Given
        var json = format("[{\"\":\"%s\"}]", VALUE);

        // Then
        assertThatThrownBy(() -> mapperWithModule().readValue(json, ConfigEntry.class))
                .isInstanceOf(JsonProcessingException.class)
                .hasMessageContaining(
                        "`ARRAY` out of START_OBJECT token\n"
                                + " at [Source: (String)\"[{\"\":\"DAYS\"}]\"; line: 1, column: 13]");
    }
}
