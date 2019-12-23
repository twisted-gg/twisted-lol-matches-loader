package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.frames.MatchParticipantFramesPosition
import com.twisted.dto.summoner.SummonerDocument
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import org.bson.types.ObjectId

fun findSummonerByParticipantId(participantId: Int, match: Match, participants: List<SummonerDocument>): ObjectId {
  val participantIdentity = match.participantIdentities.find { p -> p.participantId == participantId }
          ?: throw Exception()
  val summoner = participants.find { s -> s.accountId == participantIdentity.player.currentAccountId }
          ?: throw Exception()
  return ObjectId(summoner._id)
}

fun findAssistingIds(event: MatchEvent, match: Match, participants: List<SummonerDocument>) = event.assistingParticipantIds.map { id -> findSummonerByParticipantId(id, match, participants) }

fun getPosition(event: MatchEvent) = MatchParticipantFramesPosition(
        x = event.position.x,
        y = event.position.y
)
