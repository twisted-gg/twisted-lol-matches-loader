package com.twisted.lolmatches_loader.riot

import com.twisted.lolmatches_loader.summoners.dto.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component

const val MAX_THREADS = 4

@Component
class RiotService {
  private val apiKey = System.getenv("API_KEY") ?: ""

  fun parseRegion(value: ListRegions): Platform {
    return parseRegion(value.toString())
  }

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

  fun getAsynApi() = getApi().asyncApi ?: throw Exception()
}