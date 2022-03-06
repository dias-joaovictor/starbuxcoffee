package com.bestseller.starbuxcoffee.core.config;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration//
				.defaultCacheConfig()//
				.entryTtl(Duration.ofMinutes(60))//
				.disableCachingNullValues() //
				.serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

}
