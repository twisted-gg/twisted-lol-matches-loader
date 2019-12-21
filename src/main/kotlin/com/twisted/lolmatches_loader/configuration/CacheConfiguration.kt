package com.twisted.lolmatches_loader.configuration

import com.google.common.base.Joiner.on
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfiguration(
        val redisConnectionFactory: RedisConnectionFactory,
        // Default 1 month of ttl
        @Value("\${spring.cache.time-to-live:2628000000}") val ttl: Long
) : CachingConfigurerSupport() {
  @Bean
  fun redisCacheConfiguration(): RedisCacheConfiguration {
    return RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMillis(ttl))
  }

  @Bean
  override fun cacheManager(): CacheManager {
    return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration())
            .transactionAware()
            .build()
  }

  @Bean
  override fun keyGenerator(): KeyGenerator {
    return KeyGenerator { target, method, params ->
      return@KeyGenerator "%s.%s(%s)".format(target::class.qualifiedName,
              method.name,
              on(",").skipNulls().join(params))
    }
  }
}
