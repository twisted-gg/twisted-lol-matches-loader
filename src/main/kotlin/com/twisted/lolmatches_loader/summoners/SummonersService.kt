package com.twisted.lolmatches_loader.summoners

import com.twisted.dto.summoner.GetMultipleSummoner
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches_loader.errors.NotFoundException
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.CompletableFuture

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

  // Parallel requests
  @Async
  fun getSummonerList(params: List<GetSummonerRequest>): CompletableFuture<GetMultipleSummoner> {
    var url = UriComponentsBuilder.fromHttpUrl("${this.baseUrl}/multi")
            .queryParam("region", params[0].region)
    for (param in params) {
      url
              .queryParam("summonerName", param.summonerName)
              .queryParam("accountID", param.accountID)
    }
    return CompletableFuture.supplyAsync {
      RestTemplate().getForObject<GetMultipleSummoner>(url.toUriString(), GetMultipleSummoner::class.java)
              ?: throw NotFoundException()
    }
  }
}