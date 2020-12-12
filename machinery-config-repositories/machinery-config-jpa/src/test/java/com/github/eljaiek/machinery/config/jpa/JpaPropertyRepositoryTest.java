package com.github.eljaiek.machinery.config.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = ConfigJpaModuleConfiguration.class)
class JpaPropertyRepositoryTest {

  String key = "mail.server.alias";
  String value = "Administrator";
  String namespace = "mail.server";

  @Autowired TestEntityManager entityManager;
  @Autowired JpaPropertyRepository propertyRepository;

  @AfterEach
  void tearDown() {
    entityManager.clear();
  }

  @Test
  void getValueShouldNotReturnEmpty() {
    // When
    entityManager.persist(new PropertyEntity(key, value));

    // Then
    assertThat(propertyRepository.getValue(key)).isEqualTo(value);
  }

  @Test
  void findAllByNamespaceShouldNotReturnEmpty() {
    // Given
    var expected =
        Map.of(
            key,
            value,
            "mail.server.enabled",
            "true",
            "mail.server.host",
            "smtp.googlemail.com",
            "mail.server.port",
            "587",
            "mail.server.user.name",
            "admin@gmail.com",
            "mail.server.user.password",
            "admin123");

    // When
    propertyRepository.save(expected);
    var actual = propertyRepository.findAllByNamespace(namespace);

    // Then
    assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
  }

  @Test
  void saveShouldUpdateProperty() {
    // When
    propertyRepository.save(key, value);
    propertyRepository.save(key, "Admin");
    var entity = entityManager.find(PropertyEntity.class, key);

    // Then
    assertThat(entity.getValue()).isEqualTo("Admin");
  }

  @Test
  void removeShouldSucceed() {
    // When
    entityManager.persist(new PropertyEntity(key, value));
    propertyRepository.remove(key);

    // Then
    assertThat(entityManager.find(PropertyEntity.class, key)).isNull();
  }

  @Test
  void removeAllByNameSpaceShouldSucceed() {
    // Given
    var props =
        Map.of(
            key,
            value,
            "mail.server.enabled",
            "true",
            "mail.server.host",
            "smtp.googlemail.com",
            "mail.server.port",
            "587",
            "mail.server.user.name",
            "admin@gmail.com",
            "mail.server.user.password",
            "admin123");

    // When
    propertyRepository.save(props);
    propertyRepository.removeAllByNameSpace(namespace);

    // Then
    assertThat(propertyRepository.findAllByNamespace(namespace)).isEmpty();
  }
}
