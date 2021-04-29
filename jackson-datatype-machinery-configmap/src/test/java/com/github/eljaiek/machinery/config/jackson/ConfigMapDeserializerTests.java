package com.github.eljaiek.machinery.config.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.eljaiek.machinery.config.core.ConfigEntry;
import com.github.eljaiek.machinery.config.core.ConfigMap;
import java.util.function.Predicate;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class ConfigMapDeserializerTests extends ModuleTestBase {

    @Test
    void deserialize() throws JsonProcessingException {
        // Given
        var json = "[{\"mail.server.enabled\":\"true\"},{\"mail.server.alias\":\"Admin\"}]";

        // When
        var configMap = mapperWithModule().readValue(json, ConfigMap.class);

        // Then
        assertThat(configMap.entries())
                .anyMatch(assertConfigEntry("mail.server.enabled", "true"))
                .anyMatch(assertConfigEntry("mail.server.alias", "Admin"));
    }

    static Predicate<ConfigEntry> assertConfigEntry(String key, String value) {
        return entry -> entry.hasKey(key) && entry.hasValue(value);
    }

    @Test
    void deserializeShouldTFailWhenDataIsNotAnArray() {
        // Given
        var json = "{\"mail.server.enabled\":\"true\"}";

        // When
        ThrowingCallable callable = () -> mapperWithModule().readValue(json, ConfigMap.class);

        // Then
        assertThatThrownBy(callable)
                .isInstanceOf(JsonProcessingException.class)
                .hasMessage(
                        "`com.github.eljaiek.machinery.config.core.ConfigEntry` out of START_ARRAY token\n"
                                + " at [Source: (String)\"{\"mail.server.enabled\":\"true\"}\"; line: 1, column: 30]");
    }
}
