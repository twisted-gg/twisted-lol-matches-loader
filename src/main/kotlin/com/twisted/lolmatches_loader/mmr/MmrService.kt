package com.twisted.lolmatches_loader.summoners

import com.twisted.dto.mmr.MMRToLeagueDocument
import com.twisted.lolmatches_loader.errors.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class MmrService {
  private val baseUrl = System.getenv("MMR_SERVICE")

  fun mmrToLeague(mmr: Int): MMRToLeagueDocument {
    val url = UriComponentsBuilder.fromHttpUrl("${this.baseUrl}/mmr-to-league")
            .queryParam("mmr", mmr)
    return RestTemplate().getForObject<MMRToLeagueDocument>(url.toUriString(), MMRToLeagueDocument::class.java)
            ?: throw NotFoundException()
  }
}