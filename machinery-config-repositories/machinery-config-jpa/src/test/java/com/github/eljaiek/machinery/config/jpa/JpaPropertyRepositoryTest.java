package com.github.eljaiek.machinery.config.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = JpaModuleConfiguration.class)
class JpaPropertyRepositoryTest {

    final String key = "mail.server.alias";
    final String value = "Administrator";
    final String namespace = "mail.server";

    @Autowired TestEntityManager entityManager;
    @Autowired JpaPropertyRepository propertyRepository;

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
}
