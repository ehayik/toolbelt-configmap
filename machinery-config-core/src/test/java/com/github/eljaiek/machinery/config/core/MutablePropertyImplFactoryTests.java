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
class MutablePropertyImplFactoryTests {

  final String key = "mail.smtp.host";
  final String value = "smtp.mailtrap.io";

  MutablePropertyFactory mutablePropertyFactory;
  @Mock PropertyRepository propertyRepository;

  @BeforeEach
  void setUp() {
    mutablePropertyFactory = new MutablePropertyFactoryImpl(propertyRepository);
  }

  @Test
  void createShouldReturnProperty() {
    // When
    var actual = mutablePropertyFactory.create(key, value);

    // Then
    assertThat(actual)
        .isNotNull()
        .hasFieldOrPropertyWithValue("key", key)
        .hasFieldOrPropertyWithValue("value", value);
  }

  @Test
  void createShouldFailWhenKeyIsNull() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> mutablePropertyFactory.create(null, ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldFailWhenKeyIsEmpty() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> mutablePropertyFactory.create("", ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldFailWhenKeyIsBlank() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> mutablePropertyFactory.create("  ", ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldConvertMapEntryToProperty() {
    // Given
    var mapEntry = Map.of(key, value).entrySet().iterator().next();

    // When
    var actual = mutablePropertyFactory.create(mapEntry);

    // Then
    assertThat(actual)
        .isNotNull()
        .hasFieldOrPropertyWithValue("key", key)
        .hasFieldOrPropertyWithValue("value", value);
  }

  @Test
  void createShouldLoadValueFromRepository() {
    // When
    when(propertyRepository.getValue(key)).thenReturn(value);
    var actual = mutablePropertyFactory.create(key);

    // Then
    assertThat(actual.value()).get().isEqualTo(value);
  }
}
