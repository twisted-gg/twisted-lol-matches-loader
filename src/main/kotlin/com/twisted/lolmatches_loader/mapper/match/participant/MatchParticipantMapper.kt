package com.twisted.lolmatches_loader.mapper.match.participant

import com.twisted.dto.errors.InvalidEnum
import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match.participant.MatchParticipantSummoner
import com.twisted.dto.match.participant.events.MatchParticipantEvents
import com.twisted.dto.match.participant.stats.MatchParticipantKDA
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.enum.GetMapGeyFromValue
import com.twisted.enum.common.ListRegions
import com.twisted.enum.match.participants.MatchParticipantsLane
import com.twisted.enum.match.participants.MatchParticipantsRole
import com.twisted.lolmatches_loader.mapper.match.participant.events.matchParticipantEventMapper
import com.twisted.lolmatches_loader.mapper.match.participant.frames.matchParticipantFrames
import com.twisted.lolmatches_loader.mapper.match.participant.items.participantItems
import com.twisted.lolmatches_loader.mapper.match.participant.perks.participantPerks
import com.twisted.lolmatches_loader.mapper.match.participant.spells.getParticipantSpells
import com.twisted.lolmatches_loader.mapper.match.participant.stats.participantStats
import com.twisted.lolmatches_loader.summoners.SummonersService
import kotlinx.coroutines.runBlocking
import net.rithms.riot.api.endpoints.match.dto.*
import org.bson.types.ObjectId

private val summonersService = SummonersService()

/**
 * Participant KDA
 * Kill + assists / death
 */
private fun participantKDA(stats: ParticipantStats): MatchParticipantKDA {
  val kda = (stats.kills + stats.assists) / stats.deaths.toFloat()
  return MatchParticipantKDA(
          kills = stats.kills,
          assists = stats.assists,
          deaths = stats.deaths,
          kda = kda
  )
}

/**
 * Only save summoner details
 */
private fun mapSummoner(summoner: SummonerDocument): MatchParticipantSummoner =
        MatchParticipantSummoner(
                _id = ObjectId(summoner._id),
                name = summoner.name,
                level = summoner.summonerLevel
        )

/**
 * Get participant details
 */
private fun getParticipantDetails(match: Match, participantId: Int): Participant =
        match.participants.find { p -> p.participantId == participantId }
                ?: throw Exception()

fun getSummonerList(match: Match): List<SummonerDocument> {
  val params = mutableListOf<GetSummonerRequest>()
  for (participant in match.participantIdentities) {
    params.add(GetSummonerRequest(
            region = ListRegions.valueOf(match.platformId),
            summonerName = participant.player.summonerName,
            accountID = participant.player.currentAccountId
    ))
  }
  return runBlocking {
    summonersService.getSummonerList(params)
  }
}

private fun mapInstance(match: Match, matchFrames: MatchTimeline, summoner: SummonerDocument, participantId: Int, events: MatchParticipantEvents): MatchParticipant {
  val participant = getParticipantDetails(match = match, participantId = participantId)
  val frames = matchParticipantFrames(
          frames = matchFrames.frames,
          participantId = participantId
  )
  val spells = getParticipantSpells(participant)
  return MatchParticipant(
          summoner = mapSummoner(summoner),
          championId = participant.championId,
          lane = GetMapGeyFromValue(MatchParticipantsLane, participant.timeline.lane),
          role = GetMapGeyFromValue(MatchParticipantsRole, participant.timeline.role),
          teamId = participant.teamId,
          spells = spells,
          stats = participantStats(participant.stats),
          items = participantItems(participant.stats),
          perks = participantPerks(participant.stats),
          kda = participantKDA(participant.stats),
          frames = frames,
          events = events
  )
}

/**
 * Find summoner
 */
fun findSummonerByParticipant(participant: ParticipantIdentity, list: List<SummonerDocument>) = list.find { p -> p.accountId == participant.player.currentAccountId }

/**
 * Get match participants
 */
fun matchParticipants(match: Match, matchFrames: MatchTimeline): List<MatchParticipant> =
        try {
          val response = mutableListOf<MatchParticipant>()
          val participantsList = getSummonerList(match)
          for (participant in match.participantIdentities) {
            val summoner = findSummonerByParticipant(participant, participantsList)
                    ?: throw Exception()
            val events = matchParticipantEventMapper(
                    match = match,
                    frames = matchFrames,
                    participantId = participant.participantId,
                    participants = participantsList
            )
            response.add(mapInstance(
                    match = match,
                    matchFrames = matchFrames,
                    summoner = summoner,
                    participantId = participant.participantId,
                    events = events
            ))
          }
          response
        } catch (e: InvalidEnum) {
          println("Invalid enum key: ${e.message}")
          mutableListOf()
        } catch (e: Exception) {
          mutableListOf()
        }
