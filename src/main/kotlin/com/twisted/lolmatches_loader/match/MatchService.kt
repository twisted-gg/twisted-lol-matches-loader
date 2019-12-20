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
  // Internal methods
  private fun setLoadedMatch(match: MatchDocument) {
    val matchesLoading = loadingRepository.findMatch(match.game_id, match.region)
    for (matchLoading in matchesLoading) {
      for (oneMatch in matchLoading.matches) {
        if (match.game_id == oneMatch.game_id) {
          oneMatch.loading = false
        }
      }
      loadingRepository.save(matchLoading)
      repository.save(match)
    }
  }

  private fun existsByGameIdAndRegion(gameId: Long, region: Platform) = repository.existsByGameId(
          gameId = gameId,
          region = region.toString()
  )

  private fun filterMatches(matchLoading: MatchLoadingDocument): List<MatchLoadingMatches> = matchLoading.matches.filter { m -> m.loading }

  // External api methods
  private fun getMatchesTimeline(matchList: List<Match>, region: Platform): Map<Long, MatchTimeline> {
    val response = mutableMapOf<Long, MatchTimeline>()
    val asyncApi = riotApi.getAsynApi()
    for (match in matchList) {
      asyncApi.getTimelineByMatchId(region, match.gameId).also {
        it.addListeners(object : RequestAdapter() {
          override fun onRequestSucceeded(request: AsyncRequest) {
            response[match.gameId] = request.getDto<MatchTimeline>()
          }
        })
      }
    }
    asyncApi.awaitAll()
    return response
  }

  private fun getAllMatchesDetails(matchList: List<MatchLoadingMatches>, region: Platform): List<Match> {
    val asyncApi = riotApi.getAsynApi()
    val allMatchDetails = mutableListOf<Match>()
    val matchListFiltered = matchList.filter { m ->
      !existsByGameIdAndRegion(m.game_id, region)
    }
    for (match in matchListFiltered) {
      asyncApi.getMatch(region, match.game_id).also {
        it.addListeners(object : RequestAdapter() {
          override fun onRequestSucceeded(request: AsyncRequest) {
            allMatchDetails.add(request.getDto<Match>())
          }
        })
      }
    }
    asyncApi.awaitAll()
    return allMatchDetails
  }

  private fun loadAllMatches(matches: List<Match>, region: Platform): Int {
    val matchesTimeline = getMatchesTimeline(matches, region)
    for (match in matches) {
      val timeline = matchesTimeline[match.gameId] ?: continue
      val document = matchToDocument(
              match = match,
              matchTimeline = timeline
      )
      setLoadedMatch(document)
    }
    return matches.size
  }

  // Public methods
  @Async
  fun loadMatches(matchLoading: MatchLoadingDocument) {
    val region = riotApi.parseRegion(matchLoading.region)
    val loadingMatches = filterMatches(matchLoading)
    val matchesDetails = getAllMatchesDetails(loadingMatches, region)
    loadAllMatches(matchesDetails, region)
  }

  fun loadMatchesById(id: String) {
    val match = loadingRepository.findById(id).get()
    loadMatches(match)
  }
}