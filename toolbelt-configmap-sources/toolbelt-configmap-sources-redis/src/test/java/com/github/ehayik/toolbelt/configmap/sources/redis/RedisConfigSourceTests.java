package com.github.ehayik.toolbelt.configmap.sources.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@TestMethodOrder(MethodName.class)
@ContextConfiguration(classes = RedisModuleTestConfiguration.class)
class RedisConfigSourceTests {

    static final String KEY = "mail.server.alias";
    static final String VALUE = "Administrator";
    static final String PREFIX = "mail.server";

    @Autowired RedisConfigSource redisConfigSource;
    @Autowired ConfigEntryHashRepository configEntryHashRepository;

    @AfterEach
    void tearDown() {
        configEntryHashRepository.deleteAll();
    }

    @Test
    void getValueShouldReturnExpectedValue() {
        // When
        configEntryHashRepository.save(new ConfigEntryHash(KEY, VALUE));

        // Then
        assertThat(redisConfigSource.getValue(KEY)).isEqualTo(VALUE);
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
        redisConfigSource.save(expected);
        redisConfigSource.save("server.url", "https://localhost");
        var actual = redisConfigSource.groupBy(PREFIX);

        // Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void saveShouldUpdateValueOfExistingProperty() {
        // When
        redisConfigSource.save(KEY, VALUE);
        redisConfigSource.save(KEY, "Admin");
        var entity = configEntryHashRepository.findById(KEY).orElseThrow();

        // Then
        assertThat(entity.getValue()).isEqualTo("Admin");
    }
}
