package com.twisted.lolmatches_loader.mapper.match.league

import com.twisted.dto.league.MatchLeagueDocument
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches_loader.summoners.MmrService

private val mmrService = MmrService()

private fun getMMRAverage(queueId: Int, participants: List<SummonerDocument>): Int {
  var count = 0
  var totalMmr = 0
  for (participant in participants) {
    val league = participant.leagues.findLast { l -> l.queueId == queueId } ?: continue
    totalMmr += league.mmr
    count++
  }
  if (count == 0) {
    return -1
  }
  // Average mmr
  return totalMmr / count
}

fun matchLeagueMapper(queueId: Int, participants: List<SummonerDocument>): MatchLeagueDocument {
  val mmrAverage = getMMRAverage(queueId, participants)
  val league = mmrService.mmrToLeague(mmrAverage)
  return MatchLeagueDocument(
          tier = league.tier,
          rank = league.rank,
          points = league.points,
          mmr = mmrAverage
  )
}
