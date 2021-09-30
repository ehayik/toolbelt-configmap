package com.github.eljaiek.toolbelt.configmap.jackson;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.eljaiek.toolbelt.configmap.ConfigEntry;
import com.github.eljaiek.toolbelt.configmap.ConfigMap;
import com.github.eljaiek.toolbelt.configmap.ConfigMaps;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import lombok.SneakyThrows;

@SuppressWarnings("java:S1948")
class ConfigMapDeserializer extends StdDeserializer<ConfigMap> {

    private final ConfigMaps configMaps;

    protected ConfigMapDeserializer(ConfigMaps configMaps) {
        super(ConfigMap.class);
        this.configMaps = configMaps;
    }

    @Override
    public ConfigMap deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode jn = jp.getCodec().readTree(jp);

        if (!jn.isArray()) {
            ctxt.handleUnexpectedToken(
                    ConfigMap.class,
                    START_ARRAY,
                    jp,
                    "`%s` out of %s token",
                    ConfigEntry.class.getName(),
                    START_ARRAY);
            return null; // never gets here
        }

        return toStream(jn.elements())
                .map(node -> asConfigEntry(node, jp.getCodec(), ctxt))
                .collect(configMaps::of, ConfigMap::put, (x, y) -> {});
    }

    private static <T> Stream<T> toStream(Iterator<T> iterator) {
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(x -> iterator.next());
    }

    @SneakyThrows
    private ConfigEntry asConfigEntry(
            JsonNode jsonNode, ObjectCodec codec, DeserializationContext ctxt) {
        var jp = jsonNode.traverse();
        jp.setCodec(codec);
        return ctxt.readValue(jp, ConfigEntry.class);
    }
}
