package com.github.eljaiek.machinery.config.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class ConfigEntryFactoryTests {

    static final String ERROR_MESSAGE = "ConfigEntry key cannot be null or blank.";
    static final String KEY = "mail.smtp.host";
    static final String VALUE = "smtp.mailtrap.io";

    ConfigEntryFactory configEntryFactory;
    @Mock ConfigEntryRepository configEntryRepository;

    @BeforeEach
    void setUp() {
        configEntryFactory = new ConfigEntryFactoryImpl(configEntryRepository);
    }

    @Test
    void createShouldReturnProperty() {
        // When
        var actual = configEntryFactory.create(KEY, VALUE);

        // Then
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("key", KEY)
                .hasFieldOrPropertyWithValue("value", VALUE);
    }

    @Test
    void createShouldFailWhenKeyIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configEntryFactory.create(null, ""))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void createShouldFailWhenKeyIsEmpty() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configEntryFactory.create("", ""))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void createShouldFailWhenKeyIsBlank() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configEntryFactory.create("  ", ""))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void createShouldConvertMapEntryToProperty() {
        // Given
        var mapEntry = Map.of(KEY, VALUE).entrySet().iterator().next();

        // When
        var actual = configEntryFactory.create(mapEntry);

        // Then
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("key", KEY)
                .hasFieldOrPropertyWithValue("value", VALUE);
    }

    @Test
    void createShouldLoadValueFromRepository() {
        // When
        when(configEntryRepository.getValue(KEY)).thenReturn(VALUE);
        var actual = configEntryFactory.create(KEY);

        // Then
        assertThat(actual.value()).get().isEqualTo(VALUE);
    }
}
