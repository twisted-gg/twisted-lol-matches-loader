package com.twisted.lolmatches_loader.match

import com.twisted.lolmatches_loader.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches_loader.summoners.SummonersService
import com.twisted.lolmatches_loader.summoners.dto.SummonerDto
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MatchSchedule(
        private val loadingRepository: MatchLoadingRepository,
        private val summonerService: SummonersService,
        private val service: MatchService
) {
  @Scheduled(fixedRate = 1000)
  fun findMatchToLoad() {
    val match = loadingRepository.findOneByLoading() ?: return
    var summoner: SummonerDto
    try {
      summoner = summonerService.getSummonerById(match.summoner).get()
    } catch (e: Exception) {
      loadingRepository.delete(match)
      return
    }
    service.loadMatches(match, summoner)
  }

  @Scheduled(fixedRate = 5000)
  fun updateDeadMatchLoading() {
    val matchesLoading = loadingRepository.findByLoading()
    for (matchLoading in matchesLoading) {
      // Check if exists matches in database
      if (!service.isLoadedMatchLoading(matchLoading)) continue
      // Set loading as non-loading
      for (match in matchLoading.matches) {
        match.loading = false
      }
      matchLoading.loading = false
      loadingRepository.save(matchLoading)
    }
  }
}