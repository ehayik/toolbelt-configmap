package com.github.eljaiek.machinery.config.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ConfigMapSerializerTests extends ModuleTestBase {

    @Test
    void serialize() throws JsonProcessingException {
        // Given
        var configMap =
                configMaps()
                        .of(Map.of("mail.server.alias", "Admin", "mail.server.enabled", "true"));

        // When
        var json = mapperWithModule().writeValueAsString(configMap);

        // Then
        assertThat(json)
                .isEqualTo(
                        "[{\"mail.server.enabled\":\"true\"},{\"mail.server.alias\":\"Admin\"}]");
    }
}
