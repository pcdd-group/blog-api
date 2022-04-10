package work.pcdd.blogapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置类
 *
 * @author AD
 */
@Configuration
public class RedisConfig {

    @Value("${pcdd.method-expire}")
    private Long expire;

    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    private static final GenericJackson2JsonRedisSerializer JSON_SERIALIZER = new GenericJackson2JsonRedisSerializer();

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 往容器中添加RedisCacheManager容器，并设置序列化方式
     * 基于SpringBoot2 对 RedisCacheManager 的自定义配置
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存过期时间（天）
                .entryTtl(Duration.ofDays(expire))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(STRING_SERIALIZER))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(JSON_SERIALIZER));

        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(config)
                .build();
    }

    /**
     * 往容器中添加RedisTemplate对象，设置序列化方式
     * 如果没有这个Bean，则Redis可视化工具中的中文内容都会以二进制(HEX)存储，不宜检查
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置key的序列化方式为string
        redisTemplate.setKeySerializer(STRING_SERIALIZER);
        // 设置value的序列化方式为json
        redisTemplate.setValueSerializer(JSON_SERIALIZER);

        // 设置Hash key的序列化方式为string
        redisTemplate.setHashKeySerializer(STRING_SERIALIZER);
        // 设置Hash value的序列化方式为json
        redisTemplate.setHashValueSerializer(JSON_SERIALIZER);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 注册成Bean被spring管理，如果没有这个Bean，则Redis可视化工具中的中文内容都会以二进制存储，不宜检查
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        // 配置stringRedisTemplate
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }


}
