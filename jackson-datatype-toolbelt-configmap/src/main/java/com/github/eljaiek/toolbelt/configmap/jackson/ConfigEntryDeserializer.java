package com.github.eljaiek.toolbelt.configmap.jackson;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.eljaiek.toolbelt.configmap.ConfigEntry;
import com.github.eljaiek.toolbelt.configmap.ConfigMaps;
import java.io.IOException;
import lombok.NonNull;

@SuppressWarnings("java:S1948")
final class ConfigEntryDeserializer extends StdDeserializer<ConfigEntry> {

    private final ConfigMaps configMaps;

    ConfigEntryDeserializer(@NonNull ConfigMaps configMaps) {
        super(ConfigEntry.class);
        this.configMaps = configMaps;
    }

    @Override
    public ConfigEntry deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode jn = jp.getCodec().readTree(jp);

        if (!jn.isObject()) {
            ctxt.handleUnexpectedToken(
                    ConfigEntry.class,
                    START_OBJECT,
                    jp,
                    "`%s` out of %s token",
                    jn.getNodeType().name(),
                    START_OBJECT);
            return null; // never gets here
        }

        try {
            var nodeEntry = requireNonNull(jn.fields().next());
            return configMaps.of(nodeEntry.getKey(), nodeEntry.getValue().asText());
        } catch (NullPointerException | IllegalArgumentException ex) {
            ctxt.handleInstantiationProblem(ConfigEntry.class, jn.fields().next(), ex);
            return null; // never gets here
        }
    }
}
