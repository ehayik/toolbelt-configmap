package com.github.ehayik.toolbelt.configmap.jackson;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.github.ehayik.toolbelt.configmap.ConfigEntry;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class ConfigEntryDeserializerTests extends ModuleTestBase {

    static final String KEY = "time.unit";
    static final String VALUE = DAYS.toString();

    @Mock JsonParser jsonParser;
    @Mock ObjectCodec objectCodec;
    @Mock JsonNode jsonNode;
    @Mock JsonNodeType jsonNodeType;
    @Mock DeserializationContext context;

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
                        "Cannot construct instance of `com.github.ehayik.toolbelt.configmap.ConfigEntry`, problem: key cannot be null or blank.\n"
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

    @Test
    @SneakyThrows
    void deserializeShouldReturnNullWhenJsonNodeIsNotAnObject() {
        // Given
        var deserializer = new ConfigEntryDeserializer(configMaps());

        // When
        mockJsonParser();
        when(jsonNode.getNodeType()).thenReturn(jsonNodeType);

        // Then
        assertThat(deserializer.deserialize(jsonParser, context)).isNull();
    }

    @Test
    @SneakyThrows
    void deserializeShouldReturnNullWhenJsoNodeFieldsIsNull() {
        // Given
        var deserializer = new ConfigEntryDeserializer(configMaps());

        // When
        mockJsonParser();
        when(jsonNode.isObject()).thenReturn(true);

        // Then
        assertThat(deserializer.deserialize(jsonParser, context)).isNull();
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    void deserializeShouldReturnNullWhenConfigEntryKeyIsInvalid() {
        // Given
        var deserializer = new ConfigEntryDeserializer(configMaps());

        // When
        mockJsonParser();
        when(jsonNode.isObject()).thenReturn(true);
        when(jsonNode.fields()).thenReturn(newJsonNodeIterator(), newJsonNodeIterator());

        // Then
        assertThat(deserializer.deserialize(jsonParser, context)).isNull();
    }

    private Iterator<Entry<String, JsonNode>> newJsonNodeIterator() {
        return Map.of("", jsonNode).entrySet().iterator();
    }

    @SneakyThrows
    private void mockJsonParser() {
        when(jsonParser.getCodec()).thenReturn(objectCodec);
        when(objectCodec.readTree(jsonParser)).thenReturn(jsonNode);
    }

    @Test
    void newShouldThrowNullPointerException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ConfigEntryDeserializer(null))
                .withMessage("configMaps is marked non-null but is null");
    }
}
