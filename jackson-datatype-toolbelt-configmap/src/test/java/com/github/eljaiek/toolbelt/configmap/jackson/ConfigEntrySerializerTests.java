package com.github.eljaiek.toolbelt.configmap.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

class ConfigEntrySerializerTests extends ModuleTestBase {

    @Test
    void serialize() throws JsonProcessingException {
        // Given
        var configEntry = configMaps().of("server.url", "http://localhost");

        // When
        String json = mapperWithModule().writeValueAsString(configEntry);

        // Then
        assertThat(json).isEqualTo("{\"server.url\":\"http://localhost\"}");
    }
}
