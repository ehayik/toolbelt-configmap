package com.github.eljaiek.machinery.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.eljaiek.machinery.config.core.ConfigMaps;
import com.github.eljaiek.machinery.config.core.ConfigSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class ModuleTestBase {

    @Mock ConfigSource configEntryRepository;

    protected ObjectMapper mapperWithModule() {
        return mapperBuilder().build();
    }

    protected MapperBuilder<?, ?> mapperBuilder() {
        return JsonMapper.builder()
                .addModule(new ConfigMapModule(new ConfigMaps(configEntryRepository)));
    }

    protected ConfigMaps configMaps() {
        return new ConfigMaps(configEntryRepository);
    }
}
