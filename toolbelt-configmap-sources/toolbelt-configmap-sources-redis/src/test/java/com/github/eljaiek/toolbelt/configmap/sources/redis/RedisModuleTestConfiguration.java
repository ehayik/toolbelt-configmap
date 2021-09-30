package com.github.eljaiek.toolbelt.configmap.sources.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@ComponentScan
@TestConfiguration
@PropertySource("application.properties")
class RedisModuleTestConfiguration {

    private final RedisServer redisServer;
    private final RedisProperties redisProperties;

    public RedisModuleTestConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        redisServer =
                new RedisServerBuilder()
                        .port(redisProperties.port)
                        .setting("maxmemory 256M")
                        .build();
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.host, redisProperties.port);
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }
}
