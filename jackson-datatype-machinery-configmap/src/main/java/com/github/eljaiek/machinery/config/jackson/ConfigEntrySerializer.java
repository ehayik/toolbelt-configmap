package com.github.eljaiek.machinery.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.eljaiek.machinery.config.core.ConfigEntry;
import java.io.IOException;

final class ConfigEntrySerializer extends StdSerializer<ConfigEntry> {

    ConfigEntrySerializer() {
        super(ConfigEntry.class);
    }

    @Override
    public void serialize(ConfigEntry configEntry, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeRaw(configEntry.toJson());
    }
}
