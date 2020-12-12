package com.github.eljaiek.machinery.config.redis;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@ContextConfiguration(classes = RedisTestServerConfig.class)
class RedisPropertyRepositoryTest {

  private static RedisServer redisServer;

  @BeforeClass
  public static void startRedisServer() {
    redisServer = new RedisServerBuilder().port(6379).setting("maxmemory 256M").build();
    redisServer.start();
  }

  @AfterClass
  public static void stopRedisServer() {
    redisServer.stop();
  }

  @BeforeEach
  void setUp() {}

  @Test
  void getValue() {}

  @Test
  void findAllByNamespace() {}

  @Test
  void save() {}

  @Test
  void testSave() {}

  @Test
  void remove() {}

  @Test
  void removeAllByNameSpace() {}
}
