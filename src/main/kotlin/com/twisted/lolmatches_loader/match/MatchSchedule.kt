package com.twisted.lolmatches_loader.match

import com.twisted.lolmatches_loader.entity.match_loading.MatchLoadingRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MatchSchedule(
        private val loadingRepository: MatchLoadingRepository,
        private val service: MatchService
) {
  @Scheduled(fixedRate = 5000)
  fun findMatchToLoad() {
    val match = loadingRepository.findUnhealthy() ?: return
    service.loadMatches(match)
  }
}