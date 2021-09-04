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
class JpaConfigSourceTests {

    static final String KEY = "mail.server.alias";
    static final String VALUE = "Administrator";
    static final String PREFIX = "mail.server";

    @Autowired TestEntityManager entityManager;
    @Autowired JpaConfigSource jpaConfigSource;

    @Test
    void getValueShouldNotReturnEmpty() {
        // When
        entityManager.persist(new ConfigEntryEntity(KEY, VALUE));

        // Then
        assertThat(jpaConfigSource.getValue(KEY)).isEqualTo(VALUE);
    }

    @Test
    void groupByShouldExpectedValues() {
        // Given
        var expected =
                Map.of(
                        KEY,
                        VALUE,
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
        jpaConfigSource.save(expected);
        var actual = jpaConfigSource.groupBy(PREFIX);

        // Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void saveShouldUpdateProperty() {
        // When
        jpaConfigSource.save(KEY, VALUE);
        jpaConfigSource.save(KEY, "Admin");
        var entity = entityManager.find(ConfigEntryEntity.class, KEY);

        // Then
        assertThat(entity.getValue()).isEqualTo("Admin");
    }
}
