package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsWard
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isWardEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.WARD_KILL.toString() == event.type || MatchEventsEnum.WARD_PLACED.toString() == event.type

fun parseWardEvent(event: MatchEvent) =
        MatchParticipantEventsWard(
                type = event.type,
                timestamp = event.timestamp,
                wardType = event.wardType
        )
