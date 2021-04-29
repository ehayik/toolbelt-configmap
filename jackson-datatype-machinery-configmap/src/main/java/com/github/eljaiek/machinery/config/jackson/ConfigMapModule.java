package com.github.eljaiek.machinery.config.jackson;

import static com.fasterxml.jackson.core.util.VersionUtil.parseVersion;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.eljaiek.machinery.config.core.ConfigEntry;
import com.github.eljaiek.machinery.config.core.ConfigMap;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConfigMapModule extends Module {

    private static final String NAME = "ConfigMapModule";

    private final ConfigMaps configMaps;

    @Override
    public String getModuleName() {
        return NAME;
    }

    @Override
    public Version version() {
        return parseVersion(
                "0.0.1", "com.github.eljaiek.machinery", "jackson-datatype-machinery-configmap");
    }

    @Override
    public void setupModule(SetupContext context) {
        var module = new SimpleModule();
        module.addDeserializer(ConfigEntry.class, new ConfigEntryDeserializer(configMaps));
        module.addDeserializer(ConfigMap.class, new ConfigMapDeserializer(configMaps));
        module.addSerializer(new ConfigEntrySerializer());
        module.addSerializer(new ConfigMapSerializer());
        module.setupModule(context);
    }
}
