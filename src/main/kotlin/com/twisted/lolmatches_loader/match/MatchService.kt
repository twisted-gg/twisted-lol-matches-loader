package com.twisted.lolmatches_loader.match

import com.twisted.dto.match_loading.MatchLoadingMatches
import com.twisted.lolmatches_loader.entity.match.MatchDocument
import com.twisted.lolmatches_loader.entity.match.MatchRepository
import com.twisted.lolmatches_loader.entity.match_loading.MatchLoadingDocument
import com.twisted.lolmatches_loader.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches_loader.mapper.match.matchToDocument
import com.twisted.lolmatches_loader.riot.RiotService
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.api.request.AsyncRequest
import net.rithms.riot.api.request.RequestAdapter
import net.rithms.riot.constant.Platform
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
class MatchService(
        private val riotApi: RiotService,
        private val repository: MatchRepository,
        private val loadingRepository: MatchLoadingRepository
) {
  private val api = riotApi.getApi()

  // Internal methods
  private fun setLoadedMatch(game_id: Long, region: Platform) {
    val matchesLoading = loadingRepository.findMatch(game_id, region.toString())
    for (matchLoading in matchesLoading) {
      for (oneMatch in matchLoading.matches) {
        if (game_id == oneMatch.game_id) {
          oneMatch.loading = false
        }
      }
      loadingRepository.save(matchLoading)
    }
  }

  private fun existsByGameIdAndRegion(gameId: Long, region: Platform) = repository.existsByGameId(
          gameId = gameId,
          region = region.toString()
  )

  private fun filterMatches(matchLoading: MatchLoadingDocument): List<MatchLoadingMatches> = matchLoading.matches.filter { m -> m.loading }

  private fun setMatchLoadingStatus(id: String, healthy: Boolean) {
    val instance = loadingRepository.findById(id).get()
    instance.healthy = healthy
    loadingRepository.save(instance)
  }

  private fun processMatches(matchLoading: MatchLoadingDocument) {
    val matches = filterMatches(matchLoading)
    val region = riotApi.parseRegion(matchLoading.region)
    for (match in matches) {
      val gameId = match.game_id
      val exists = existsByGameIdAndRegion(gameId, region)
      if (!exists) {
        val matchDetails = getMatchDetails(gameId, region)
        val matchTimeline = getMatchTimeline(gameId, region)
        repository.save(matchToDocument(
                match = matchDetails,
                matchTimeline = matchTimeline
        ))
      }
      setLoadedMatch(gameId, region)
    }
  }

  // External methods
  private fun getMatchDetails(game_id: Long, region: Platform) = api.getMatch(region, game_id)

  private fun getMatchTimeline(game_id: Long, region: Platform) = api.getTimelineByMatchId(region, game_id)

  // Public methods
  @Async
  fun loadMatches(matchLoading: MatchLoadingDocument) {
    try {
      processMatches(matchLoading)
      setMatchLoadingStatus(id = matchLoading.id, healthy = true)
    } catch (e: Exception) {
      setMatchLoadingStatus(id = matchLoading.id, healthy = false)
    }
  }

  fun loadMatchesById(id: String) {
    val match = loadingRepository.findById(id).get()
    Thread(Runnable {
      loadMatches(match)
    }).start()
  }
}