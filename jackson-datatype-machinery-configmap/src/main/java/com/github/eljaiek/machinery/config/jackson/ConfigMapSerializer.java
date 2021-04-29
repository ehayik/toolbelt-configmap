package com.github.eljaiek.machinery.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.eljaiek.machinery.config.core.ConfigMap;
import java.io.IOException;

class ConfigMapSerializer extends StdSerializer<ConfigMap> {

    protected ConfigMapSerializer() {
        super(ConfigMap.class);
    }

    @Override
    public void serialize(ConfigMap configMap, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeRaw(configMap.toJson());
    }
}
