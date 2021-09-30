package com.github.ehayik.toolbelt.configmap;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodName.class)
class TransientConfigMapTests {

    static final String KEY = "time.unit";
    static final String VALUE = DAYS.toString();

    @Mock ConfigEntry configEntry;
    @Mock Consumer<Set<ConfigEntry>> saveBatch;
    @Mock ConfigMap delegateConfigMap;
    TransientConfigMap transientConfigMap;

    @BeforeEach
    void setUp() {
        transientConfigMap = new TransientConfigMap(delegateConfigMap, saveBatch);
    }

    @Test
    void putShouldReturnProperty() {
        // When
        when(delegateConfigMap.put(KEY, VALUE)).thenReturn(configEntry);
        var actual = transientConfigMap.put(KEY, VALUE);

        // Then
        assertThat(actual).isNotNull();
        assertThat(transientConfigMap.isTransient(KEY)).isTrue();
    }

    @Test
    @SneakyThrows
    void saveShouldCallSaveEntriesConsumer() {
        // Given
        var properties = Set.of(configEntry);

        // When
        when(configEntry.key()).thenReturn(KEY);
        when(delegateConfigMap.entries(transientConfigMap.getTransientEntryKeys()))
                .thenReturn(properties);
        transientConfigMap.put(configEntry);
        transientConfigMap.save();

        // Then
        verify(saveBatch).accept(properties);
    }

    @Test
    void clearShouldRemoveAllProperties() {
        // When
        when(configEntry.key()).thenReturn(KEY);
        transientConfigMap.put(configEntry);
        transientConfigMap.clear();

        // Then
        verify(delegateConfigMap).clear();
        assertThat(transientConfigMap.hasTransientEntries()).isFalse();
    }

    @Test
    void removeShouldReturnProperty() {
        // When
        when(configEntry.key()).thenReturn(KEY);
        when(delegateConfigMap.remove(KEY)).thenReturn(Optional.of(configEntry));
        transientConfigMap.put(configEntry);
        var actual = transientConfigMap.remove(KEY);

        // Then
        assertThat(transientConfigMap.isTransient(KEY)).isFalse();
        assertThat(actual).isPresent().get().isEqualTo(configEntry);
    }

    @Test
    void removeShouldReturnEmpty() {
        // When
        when(configEntry.key()).thenReturn(KEY);
        transientConfigMap.put(configEntry);
        var actual = transientConfigMap.remove("other");

        // Then
        assertThat(transientConfigMap.isTransient(KEY)).isTrue();
        assertThat(actual).isEmpty();
    }
}
