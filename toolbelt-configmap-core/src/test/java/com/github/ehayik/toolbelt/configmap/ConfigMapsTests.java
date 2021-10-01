package com.github.ehayik.toolbelt.configmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class ConfigMapsTests {

    static final String ERROR_MESSAGE = "key cannot be null or blank.";
    static final String PREFIX = "mail";
    static final String KEY = "mail.smtp.host";
    static final String VALUE = "smtp.mailtrap.io";

    @InjectMocks ConfigMaps configMaps;
    @Mock ConfigSource configEntryRepository;
    InstanceOfAssertFactory<ConfigMap, ObjectAssert<ConfigMap>> configMapAssertFactory;

    @BeforeEach
    @SuppressWarnings({"rawtypes", "unchecked"})
    void setUp() {
        configMapAssertFactory =
                new InstanceOfAssertFactory(ConfigMap.class, Assertions::assertThat);
    }

    @Test
    void groupByShouldReturnNotEmptyConfigMap() {
        // When
        when(configEntryRepository.groupBy(PREFIX)).thenReturn(Map.of(KEY, VALUE));
        var configMap = configMaps.groupBy(PREFIX);

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", configMapAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void groupByShouldThrowIllegalArgumentExceptionWhenNamespaceIsInvalid(String namespace) {
        // Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configMaps.groupBy(namespace))
                .withMessage("prefix cannot be null or blank");
    }

    @Test
    void ofShouldReturnConfigMapContainingGivenConfigEntriesSet() {
        // When
        var configMap = configMaps.of(Set.of(new ConfigEntry(KEY, VALUE)));

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", configMapAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }

    @Test
    void ofShouldThrowNullPointerExceptionWhenTheGivenConfigEntriesSetIsNull() {
        // Given
        Set<ConfigEntry> entries = null;

        // Then
        assertThatNullPointerException()
                .isThrownBy(() -> configMaps.of(entries))
                .withMessage("entries is marked non-null but is null");
    }

    @Test
    void ofShouldReturnConfigMapContainingGivenMapEntries() {
        // When
        var configMap = configMaps.of(Map.of(KEY, VALUE));

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", configMapAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isFalse();
    }

    @Test
    void ofShouldThrowNullPointerExceptionWhenTheGivenEntriesMapIsNull() {
        // Given
        Map<String, String> entries = null;

        // Then
        assertThatNullPointerException()
                .isThrownBy(() -> configMaps.of(entries))
                .withMessage("configEntries is marked non-null but is null");
    }

    @Test
    void ofShouldReturnEmptyConfigMap() {
        // When
        var configMap = configMaps.of();

        // Then
        assertThat(configMap)
                .isInstanceOf(TransientConfigMap.class)
                .extracting("delegate", configMapAssertFactory)
                .isInstanceOf(UnifiedConfigMap.class);
        assertThat(configMap.isEmpty()).isTrue();
    }

    @Test
    void ofShouldReturnConfigEntry() {
        // When
        var actual = configMaps.of(KEY, VALUE);

        // Then
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("key", KEY)
                .hasFieldOrPropertyWithValue("value", VALUE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void ofShouldFailWhenConfigEntryKeyIsInvalid(String invalidKey) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> configMaps.of(invalidKey, ""))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void ofShouldReturnConfigEntryAndFetchItsValueFromValuesSource() {
        // When
        when(configEntryRepository.getValue(KEY)).thenReturn(VALUE);
        var actual = configMaps.of(KEY);

        // Then
        assertThat(actual.value()).get().isEqualTo(VALUE);
    }
}
