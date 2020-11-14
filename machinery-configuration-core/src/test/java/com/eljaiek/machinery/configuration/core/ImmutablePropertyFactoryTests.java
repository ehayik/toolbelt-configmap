package com.eljaiek.machinery.configuration.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(Alphanumeric.class)
class ImmutablePropertyFactoryTests {

  PropertyFactory propertyFactory;

  @BeforeEach
  void setUp() {
    propertyFactory = new ImmutablePropertyFactory(x -> {}, x -> {});
  }

  @Test
  void create() {
    // When
    var actual = propertyFactory.create("mail.smtp.host", "smtp.mailtrap.io");

    // Then
    assertThat(actual)
        .isNotNull()
        .hasFieldOrPropertyWithValue("key", "mail.smtp.host")
        .hasFieldOrPropertyWithValue("value", "smtp.mailtrap.io");
  }

  @Test
  void createShouldFailWhenKeyIsNull() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> propertyFactory.create(null, ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldFailWhenKeyIsEmpty() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> propertyFactory.create("", ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldFailWhenKeyIsBlank() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> propertyFactory.create("  ", ""))
        .withMessage("key cannot be null or blank.");
  }

  @Test
  void createShouldConvertMapEntryToProperty() {
    // Given
    var mapEntry = Map.of("mail.smtp.host", "smtp.mailtrap.io").entrySet().iterator().next();

    // When
    var actual = propertyFactory.create(mapEntry);

    // Then
    assertThat(actual)
        .isNotNull()
        .hasFieldOrPropertyWithValue("key", "mail.smtp.host")
        .hasFieldOrPropertyWithValue("value", "smtp.mailtrap.io");
  }
}
