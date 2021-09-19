package com.github.eljaiek.machinery.config.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@TestMethodOrder(MethodName.class)
class SystemConfigSourceTest {

    static final String KEY = "user.test.country";
    static final String EXPECTED_VALUE = "BG";
    static Map<String, String> CONFIG_VALUES;

    SystemConfigSource configSource;

    @BeforeAll
    static void beforeAll() {
        CONFIG_VALUES = new HashMap<>();
        CONFIG_VALUES.put(KEY, "CU");
        CONFIG_VALUES.put("user.test.home", "/home/land");
        CONFIG_VALUES.put("user.test.language", "es");
    }

    @BeforeEach
    void setUp() {
        configSource = new SystemConfigSource();
    }

    @Test
    void getValueShouldNotReturnReturnNull() {
        // When
        configSource.save(KEY, EXPECTED_VALUE);
        var actual = configSource.getValue(KEY);

        // Then
        assertThat(actual).isEqualTo(EXPECTED_VALUE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void getValueShouldThrowIllegalArgumentExceptionWhenKeyIsEmptyOrNull(String key) {
        // Given
        var expectedErrorMsg = "key can't be " + (key == null ? "null" : "empty");

        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configSource.getValue(key))
                .withMessage(expectedErrorMsg);
    }

    @Test
    void groupByShouldReturnTheExpectedValues() {
        // When
        configSource.save(CONFIG_VALUES);
        var actualValues = configSource.groupBy("user.test");

        // Then
        assertThat(actualValues).isEqualTo(CONFIG_VALUES);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void groupByShouldThrowIllegalArgumentExceptionWhenNamespaceIsNullOrEmpty(String namespace) {
        // Given
        var expectedErrorMsg = "namespace can't be " + (namespace == null ? "null" : "empty");

        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configSource.groupBy(namespace))
                .withMessage(expectedErrorMsg);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void saveShouldThrowIllegalArgumentExceptionWhenKeyIsNullOrEmpty(String key) {
        // Given
        var expectedErrorMsg = "key can't be " + (key == null ? "null" : "empty");

        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configSource.save(key, EXPECTED_VALUE))
                .withMessage(expectedErrorMsg);
    }

    @Test
    void saveThrowIllegalArgumentExceptionWhenConfigEntriesIsNull() {
        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configSource.save(null))
                .withMessage("configEntries can't be null");
    }
}
