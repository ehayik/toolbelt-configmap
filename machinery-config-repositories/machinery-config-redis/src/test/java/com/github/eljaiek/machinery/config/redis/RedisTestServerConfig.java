package com.github.eljaiek.machinery.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Import(RedisRepositoryConfiguration.class)
public class RedisTestServerConfig {

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      JedisConnectionFactory jedisConnectionFactory) {
    var template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisConnectionFactory);
    template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
    return template;
  }
}
