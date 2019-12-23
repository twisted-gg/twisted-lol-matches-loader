package com.twisted.lolmatches_loader.summoners

import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches_loader.errors.NotFoundException
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.CompletableFuture

const val ADD_MATCH_TO_SUMMONER_TYPE = "LOL"

@Component
class SummonersService {
  private val baseUrl = System.getenv("SUMMONERS_SERVICE")

  @Async
  fun getSummoner(param: GetSummonerRequest): CompletableFuture<SummonerDocument> {
    val url = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("summonerName", param.summonerName)
            .queryParam("region", param.region)
            .queryParam("accountID", param.accountID)
            .toUriString()
    return CompletableFuture.supplyAsync {
      RestTemplate().getForObject<SummonerDocument>(url, SummonerDocument::class.java)
              ?: throw NotFoundException()
    }
  }

  @Async
  fun addMatchToSummoner(summonerId: String, match_id: Long): CompletableFuture<Unit> {
    val url = UriComponentsBuilder.fromHttpUrl("${this.baseUrl}/match")
            .queryParam("summoner_id", summonerId)
            .queryParam("match_id", match_id)
            .queryParam("type", ADD_MATCH_TO_SUMMONER_TYPE)
            .toUriString()
    val rest = RestTemplate()
    rest.requestFactory = HttpComponentsClientHttpRequestFactory()

    return CompletableFuture.supplyAsync {
      rest.patchForObject(url, null, String.javaClass)
      return@supplyAsync
    }
  }

  // Parallel requests
  fun getSummonerList(params: List<GetSummonerRequest>) = params.map { param -> getSummoner(param) }.map { v -> v.get() }
}