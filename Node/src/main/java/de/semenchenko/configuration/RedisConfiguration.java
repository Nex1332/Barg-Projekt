package de.semenchenko.configuration;

import de.semenchenko.model.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, AppUser> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<AppUser> serializer = new Jackson2JsonRedisSerializer<>(AppUser.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, AppUser> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, AppUser> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
