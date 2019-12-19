package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsBuildingKill
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum
import com.twisted.lolmatches_loader.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isBuildingKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.BUILDING_KILL.toString() == event.type

fun parseBuildingKillEvent(event: MatchEvent, match: Match, participants: List<SummonerDto>): MatchParticipantEventsBuildingKill {
  val assistingIds = findAssistingIds(event, match, participants)
  val position = getPosition(event)
  return MatchParticipantEventsBuildingKill(
          timestamp = event.timestamp,
          position = position,
          assistingIds = assistingIds,
          buildingType = event.buildingType,
          laneType = event.laneType
  )
}
