package com.howiezhao.akuoj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;

/**
 * @author: LiMing
 * @since: 2020/6/20 17:27
 **/
@Configuration
public class RedisTemplateConfig {


    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        //序列化key

        template.setKeySerializer(RedisSerializer.string());

        //序列化value
        template.setValueSerializer(RedisSerializer.json());


        //序列化hash的key
        template.setHashKeySerializer(RedisSerializer.string());

        //序列化hash的value
        template.setHashValueSerializer(RedisSerializer.json());


        template.afterPropertiesSet();

        return template;

    }

}
