package com.twisted.lolmatches_loader.riot

import com.twisted.lolmatches_loader.summoners.dto.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.constant.Platform
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

const val MAX_THREADS = 4

@Component
@CacheConfig(cacheNames = ["import"], cacheManager = "cacheManager", keyGenerator = "keyGenerator")
class RiotService {
  private val apiKey = System.getenv("API_KEY") ?: ""

  fun parseRegion(value: String): Platform {
    var region: Platform
    when (value) {
      ListRegions.LA1.toString() -> region = Platform.LAN
      else -> {
        throw Exception("Invalid region $value")
      }
    }
    return region
  }

  fun getApi(): RiotApi {
    val config = ApiConfig()
            .setKey(apiKey)
            .setMaxAsyncThreads(MAX_THREADS)
    return RiotApi(config)
  }

  @Cacheable(unless = "#result == null")
  fun getMatchDetails(game_id: Long, region: Platform): Match = getApi().getMatch(region, game_id)

  @Cacheable(unless = "#result == null")
  fun getMatchTimeline(game_id: Long, region: Platform): MatchTimeline = getApi().getTimelineByMatchId(region, game_id)
}