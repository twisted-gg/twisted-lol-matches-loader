package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsWard
import com.twisted.enum.GetMapGeyFromValue
import com.twisted.enum.match.participants.events.MatchParticipantsEventsType
import com.twisted.enum.match.participants.events.MatchParticipantsEventsWardTypes
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isWardEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.WARD_KILL.toString() == event.type || MatchEventsEnum.WARD_PLACED.toString() == event.type

fun parseWardEvent(event: MatchEvent) =
        MatchParticipantEventsWard(
                type = GetMapGeyFromValue(MatchParticipantsEventsType, event.type),
                timestamp = event.timestamp,
                wardType = GetMapGeyFromValue(MatchParticipantsEventsWardTypes, event.wardType)
        )
