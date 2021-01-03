package com.github.eljaiek.machinery.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
class RedisProperties {

  final int port;
  final String host;

  RedisProperties(
      @Value("${spring.redis.port}") int port, @Value("${spring.redis.host}") String host) {
    this.port = port;
    this.host = host;
  }
}
