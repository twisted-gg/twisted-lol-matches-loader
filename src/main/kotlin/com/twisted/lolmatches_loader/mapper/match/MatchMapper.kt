package com.twisted.lolmatches_loader.mapper.match

import com.twisted.dto.match.team.MatchTeam
import com.twisted.dto.match.team.MatchTeamBans
import com.twisted.dto.match.team.MatchTeamStats
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.enum.common.ListRegions
import com.twisted.enum.getMapGeyFromValue
import com.twisted.enum.match.MatchGameMode
import com.twisted.enum.match.MatchGameTypes
import com.twisted.lolmatches_loader.entity.match.MatchDocument
import com.twisted.lolmatches_loader.mapper.match.league.matchLeagueMapper
import com.twisted.lolmatches_loader.mapper.match.participant.matchParticipants
import com.twisted.lolmatches_loader.summoners.SummonersService
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
import net.rithms.riot.api.endpoints.match.dto.TeamStats


private val summonersService = SummonersService()

private fun teamTotalChampionKills(match: Match, teamId: Int): Int {
  val participants = match.participants.filter { p -> p.teamId == teamId }
  var response = 0
  for (participant in participants) {
    response += participant.stats.kills
  }
  return response
}

/**
 * Get team stats
 */
private fun teamStats(match: Match, team: TeamStats): MatchTeamStats {
  return MatchTeamStats(
          championKills = teamTotalChampionKills(match, team.teamId),
          baronKills = team.baronKills,
          dominionVictoryScore = team.dominionVictoryScore,
          dragonKills = team.dragonKills,
          inhibitorKills = team.inhibitorKills,
          riftHeraldKills = team.riftHeraldKills,
          towerKills = team.towerKills,
          vilemawKills = team.vilemawKills,
          firstBaron = team.isFirstBaron,
          firstBlood = team.isFirstBlood,
          firstDragon = team.isFirstDragon,
          firstInhibitor = team.isFirstInhibitor,
          firstRiftHerald = team.isFirstRiftHerald,
          firstTower = team.isFirstTower
  )
}

/**
 * Get team bans
 */
private fun teamBans(team: TeamStats): List<MatchTeamBans> {
  val response = mutableListOf<MatchTeamBans>()
  if (team.bans == null) {
    return response
  }
  for (ban in team.bans) {
    response.add(MatchTeamBans(
            pickTurn = ban.pickTurn,
            champion = ban.championId
    ))
  }
  return response
}

/**
 * Match teams
 */
private fun matchTeams(match: Match): List<MatchTeam> {
  val response = mutableListOf<MatchTeam>()
  for (team in match.teams) {
    val matchItem = MatchTeam(
            teamId = team.teamId,
            win = isWin(team.win),
            stats = teamStats(match, team),
            bans = teamBans(team)
    )
    response.add(matchItem)
  }
  return response
}

/**
 * Get summoners list from service
 */
private fun getSummonerList(match: Match): List<SummonerDocument> {
  val params = mutableListOf<GetSummonerRequest>()
  for (participant in match.participantIdentities) {
    params.add(GetSummonerRequest(
            region = ListRegions.valueOf(match.platformId),
            summonerName = participant.player.summonerName,
            accountID = participant.player.currentAccountId
    ))
  }
  return summonersService.getSummonerList(params).get().users
}

/**
 * Match to document
 * Convert match object to database document
 */
fun matchToDocument(match: Match, matchTimeline: MatchTimeline): MatchDocument {
  val participantsList = getSummonerList(match)
  val participants = matchParticipants(participantsList, match, matchTimeline)
  val badMatch = participants.count() == 0
  val league = matchLeagueMapper(match.queueId, participantsList)
  return MatchDocument(
          region = match.platformId,
          framesInterval = matchTimeline.frameInterval,
          remake = isRemake(match),
          match_break = badMatch,
          game_id = match.gameId,
          creation = match.gameCreation,
          duration = secondsToMs(match.gameDuration),
          mode = getMapGeyFromValue(MatchGameMode, match.gameMode),
          type = getMapGeyFromValue(MatchGameTypes, match.gameType),
          version = match.gameVersion,
          map_id = match.mapId,
          queue = match.queueId,
          season = match.seasonId,
          teams = matchTeams(match),
          participants = participants,
          league = league
  )
}
