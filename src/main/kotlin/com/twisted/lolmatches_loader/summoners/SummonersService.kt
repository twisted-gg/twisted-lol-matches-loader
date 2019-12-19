package com.twisted.lolmatches_loader.summoners

import com.twisted.lolmatches_loader.errors.NotFoundException
import com.twisted.lolmatches_loader.summoners.dto.GetSummonerDto
import com.twisted.lolmatches_loader.summoners.dto.SummonerDto
import org.bson.types.ObjectId
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.CompletableFuture

@Component
class SummonersService {
  private val baseUrl = System.getenv("SUMMONERS_SERVICE")
  private val rest = RestTemplate()

  @Async
  fun getSummoner(param: GetSummonerDto): CompletableFuture<SummonerDto> {
    val url = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("summonerName", param.summonerName)
            .queryParam("region", param.region)
            .queryParam("accountID", param.accountID)
            .toUriString()
    return CompletableFuture.supplyAsync {
      this.rest.getForObject<SummonerDto>(url, SummonerDto::class.java)
              ?: throw NotFoundException()
    }
  }

  @Async
  fun getSummonerById(id: ObjectId): CompletableFuture<SummonerDto> {
    val url = UriComponentsBuilder.fromHttpUrl("${this.baseUrl}/by-id/$id")
            .toUriString()
    return CompletableFuture.supplyAsync {
      this.rest.getForObject<SummonerDto>(url, SummonerDto::class.java)
              ?: throw NotFoundException()
    }
  }

  fun getSummonerList(params: List<GetSummonerDto>): List<SummonerDto> {
    val response = mutableListOf<CompletableFuture<SummonerDto>>()
    for (param in params) {
      val summoner = getSummoner(param)
      response.add(summoner)
    }
    return response.map { v -> v.get() }
  }
}