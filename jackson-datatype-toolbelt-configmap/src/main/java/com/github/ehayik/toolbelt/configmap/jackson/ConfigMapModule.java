package com.github.ehayik.toolbelt.configmap.jackson;

import static com.fasterxml.jackson.core.util.VersionUtil.parseVersion;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.ehayik.toolbelt.configmap.ConfigEntry;
import com.github.ehayik.toolbelt.configmap.ConfigMap;
import com.github.ehayik.toolbelt.configmap.ConfigMaps;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConfigMapModule extends Module {

    private static final String NAME = "ConfigMapModule";
    private static final String GROUP_ID = "com.github.ehayik.toolbelt";
    private static final String ARTIFACT_ID = "jackson-datatype-toolbelt-configmap";
    private static final String VERSION = "0.0.1";

    private final ConfigMaps configMaps;

    @Override
    public String getModuleName() {
        return NAME;
    }

    @Override
    public Version version() {
        return parseVersion(VERSION, GROUP_ID, ARTIFACT_ID);
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
