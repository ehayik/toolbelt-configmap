package com.github.eljaiek.machinery.configuration.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(Alphanumeric.class)
class ImmutablePropertyFactoryTests {

  PropertyFactory propertyFactory;
  @Mock
  PropertyRepository propertyRepository;

  @BeforeEach
  void setUp() {
    propertyFactory = new ImmutablePropertyFactory(propertyRepository);
  }

  @Test
  void createShouldReturnProperty() {
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
