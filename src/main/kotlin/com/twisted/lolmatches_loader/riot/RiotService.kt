package com.twisted.lolmatches_loader.riot

import com.twisted.enum.common.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.api.request.ratelimit.RateLimitException
import net.rithms.riot.constant.Platform
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

const val MAX_THREADS = 4

@Component
@CacheConfig(cacheNames = ["import"], cacheManager = "cacheManager", keyGenerator = "keyGenerator")
class RiotService {
  private val apiKey = System.getenv("API_KEY") ?: ""

  private fun waitRateLimit(rateLimitException: RateLimitException) {
    val seconds = (rateLimitException.retryAfter * 1000).toLong()
    Thread.sleep(seconds)
  }

  fun parseRegion(value: String): Platform {
    return when (value) {
      ListRegions.LA1.toString() -> Platform.LAN
      ListRegions.EUW1.toString() -> Platform.EUW
      else -> {
        throw Exception("Invalid region $value")
      }
    }
  }

  fun getApi(): RiotApi {
    val config = ApiConfig()
            .setKey(apiKey)
            .setMaxAsyncThreads(MAX_THREADS)
    return RiotApi(config)
  }

  @Cacheable(unless = "#result == null")
  fun getMatchDetails(game_id: Long, region: Platform): Match {
    return try {
      getApi().getMatch(region, game_id)
    } catch (e: RateLimitException) {
      waitRateLimit(e)
      getMatchDetails(game_id, region)
    }
  }

  @Cacheable(unless = "#result == null")
  fun getMatchTimeline(game_id: Long, region: Platform): MatchTimeline {
    return try {
      getApi().getTimelineByMatchId(region, game_id)
    } catch (e: RateLimitException) {
      waitRateLimit(e)
      getMatchTimeline(game_id, region)
    }
  }
}