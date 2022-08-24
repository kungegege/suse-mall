package com.frame.config;

import com.frame.serializer.MyStringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        /*指定key的序列化方式：StringRedisSerializer*/
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        /*指定value的序列化方式：MyStringRedisSerializer(自定义的序列化方式)*/
        redisTemplate.setValueSerializer(new MyStringRedisSerializer());

        /*指定key的反序列化方式：StringRedisSerializer*/
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        /*指定value的反序列化方式：MyStringRedisSerializer(自定义的序列化方式)*/
        redisTemplate.setHashValueSerializer(new MyStringRedisSerializer());
        return redisTemplate;
    }
}
